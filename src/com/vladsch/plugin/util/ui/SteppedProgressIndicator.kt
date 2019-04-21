package com.vladsch.plugin.util.ui

import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.progress.ProgressIndicator
import com.vladsch.plugin.util.maxLimit

class SteppedProgressIndicator(progressIndicator: ProgressIndicator?) : ProgressIndicator {
    private val myProgressIndicator = progressIndicator
    private var stepStart: Double = 0.0
    private var stepSize: Double = 1.0
    private var regionStart: Double = 0.0
    private var regionEnd: Double = 0.0
    private var regionPos = 0
    private var regionGradations = 1
    private var mySteps = 1
    var sleepTime = 0L

    private var myFraction: Double = 0.0
    private var myText: String? = null
    private var myText2: String? = null

    var steps: Int
        get() = mySteps
        set(value) {
            mySteps = value
            stepSize = 1.0 / mySteps
        }

    init {
        myProgressIndicator?.isIndeterminate = false
    }

    fun nextGradation(text2: String? = null, runnable: (() -> Unit)? = null) {
        this.text2 = text2 ?: ""

        if (runnable != null) {
            Thread.sleep(0)
            runnable()
            if (sleepTime > 0) Thread.sleep(sleepTime)
        }

        regionPos++
        if (regionPos > regionGradations) {
            // next step
            regionPos = regionGradations
        }
        updateFraction()
    }

    fun nextRegion(gradations: Int, steps: Int, text: String? = null, runnable: (() -> Unit)? = null) {
        stepStart = regionEnd

        regionStart = stepStart
        regionEnd = (stepStart + stepSize * steps).maxLimit(1.0)
        regionPos = 0
        regionGradations = gradations
        this.text = text ?: ""
        updateFraction()

        if (runnable != null) {
            Thread.sleep(0)
            runnable()
            if (sleepTime > 0) Thread.sleep(sleepTime)
        }
    }

    private fun updateFraction() {
        fraction = (regionStart + (regionPos.toDouble() / regionGradations) * (regionEnd - regionStart)).maxLimit(1.0)
    }

    override fun pushState() {
        myProgressIndicator?.pushState()
    }

    override fun startNonCancelableSection() {
        myProgressIndicator?.startNonCancelableSection()
    }

    override fun cancel() {
        myProgressIndicator?.cancel()
    }

    override fun checkCanceled() {
        myProgressIndicator?.checkCanceled()
    }

    override fun start() {
        myProgressIndicator?.start()
    }

    override fun finishNonCancelableSection() {
        myProgressIndicator?.finishNonCancelableSection()
    }

    override fun stop() {
        myProgressIndicator?.stop()
    }

    override fun popState() {
        myProgressIndicator?.popState()
    }

    override fun setIndeterminate(indeterminate: Boolean) {
        myProgressIndicator?.isIndeterminate = indeterminate
    }

    override fun setFraction(fraction: Double) {
        myFraction = fraction
        myProgressIndicator?.fraction = fraction
    }

    override fun setText2(text: String?) {
        myText2 = text
        myProgressIndicator?.text2 = text
    }

    override fun setModalityProgress(modalityProgress: ProgressIndicator?) {
        myProgressIndicator?.setModalityProgress(modalityProgress)
    }

    override fun setText(text: String?) {
        myText = text
        myProgressIndicator?.text = text
    }

    override fun getModalityState(): ModalityState {
        return myProgressIndicator?.modalityState ?: ModalityState.NON_MODAL
    }

    override fun isRunning(): Boolean {
        return myProgressIndicator?.isRunning ?: true
    }

    override fun isModal(): Boolean {
        return myProgressIndicator?.isModal ?: false
    }

    override fun getText(): String {
        return myProgressIndicator?.text ?: myText ?: ""
    }

    override fun isCanceled(): Boolean {
        return myProgressIndicator?.isCanceled ?: false
    }

    override fun getText2(): String {
        return myProgressIndicator?.text2 ?: myText2 ?: ""
    }

    override fun isIndeterminate(): Boolean {
        return myProgressIndicator?.isIndeterminate ?: false
    }

    override fun isPopupWasShown(): Boolean {
        return myProgressIndicator?.isPopupWasShown ?: false
    }

    override fun isShowing(): Boolean {
        return myProgressIndicator?.isShowing ?: false
    }

    override fun getFraction(): Double {
        return myProgressIndicator?.fraction ?: myFraction
    }
}
