package com.pulse.components.recipes

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import androidx.paging.PagingData
import androidx.work.*
import androidx.work.WorkInfo.State.*
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.recipes.RecipesWorker.Companion.KEY_RESULT
import com.pulse.components.recipes.RecipesWorker.Companion.KEY_VALUE
import com.pulse.components.recipes.adapter.RecipesAdapter
import com.pulse.components.recipes.model.Recipe
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.GeneralException
import kotlinx.android.synthetic.main.fragment_recipes.*
import timber.log.Timber

class RecipesFragment(private val vm: RecipesViewModel, private val workManager: WorkManager) : BaseMVVMFragment(R.layout.fragment_recipes) {

    private val recipesAdapter = RecipesAdapter(::downloadAndShow)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        rvRecipes.adapter = recipesAdapter

        recipesAdapter.addStateListener { progressCallback?.setInProgress(it) }
    }

    override fun onBindLiveData() {
        observe(vm.recipesLiveData, ::showRecipes)
        observe(vm.recipesErrorLiveData) { it.contentOrNull?.let(::showAlertOrNot) }
        observe(vm.recipesCountLiveData) { emptyContentRecipes.visibleOrGone(it <= 0) }
    }

    private fun showRecipes(pagingData: PagingData<Recipe>) = recipesAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)

    private fun showAlertOrNot(generalException: GeneralException) {
        if (generalException.resId == R.string.toSeeYourRecipes) {
            showAlertRes(getString(R.string.toSeeYourRecipes)) {
                cancelable = false
                positive = R.string.signIn
                negative = R.string.cancel

                positiveAction = { navController.navigate(R.id.fromRecipesToAuth, SignInFragmentArgs(R.id.nav_recipes).toBundle()) }
                negativeAction = { navController.onNavDestinationSelected(R.id.nav_home, inclusive = true) }
            }
        }
    }

    private fun downloadAndShow(recipe: Recipe) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startWorker(recipe)
        } else {
            permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .build()
                .apply {
                    addListener { result ->
                        if (result.allGranted()) {
                            startWorker(recipe)
                        }
                    }
                }
                .send()
        }
    }

    private fun startWorker(recipe: Recipe) {

        val work = OneTimeWorkRequestBuilder<RecipesWorker>()
            .setInputData(Data.Builder().putString(KEY_VALUE, recipe.code).build())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        workManager.enqueue(work)

        observeNullable(workManager.getWorkInfoByIdLiveData(work.id)) { workInfo ->
            workInfo?.let {
                Timber.e("State: $it")
                when (it.state) {
                    ENQUEUED -> {
                    }
                    RUNNING -> progressCallback?.setInProgress(true)
                    SUCCEEDED -> workInfo.outputData.getString(KEY_RESULT)?.let { showSucceeded(it.toUri()) }
                    FAILED, BLOCKED, CANCELLED -> progressCallback?.setInProgress(false)
                }
            }
        }
    }

    private fun showSucceeded(uri: Uri) {
        progressCallback?.setInProgress(false)

        val target = Intent(ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"))
        }

        try {
            startActivity(Intent.createChooser(target, "openTitle").apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            })
        } catch (e: ActivityNotFoundException) {
            debug { e.printStackTrace() }
        }
    }
}