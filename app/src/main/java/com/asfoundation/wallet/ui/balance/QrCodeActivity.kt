package com.asfoundation.wallet.ui.balance

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import androidx.core.app.ShareCompat
import com.asf.wallet.R
import com.asfoundation.wallet.interact.FindDefaultWalletInteract
import com.asfoundation.wallet.ui.BaseActivity
import com.asfoundation.wallet.ui.MyAddressActivity
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.jakewharton.rxbinding2.view.RxView
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.qr_code_layout.*
import javax.inject.Inject

class QrCodeActivity : BaseActivity(), QrCodeView {

  @Inject
  lateinit var findDefaultWalletInteract: FindDefaultWalletInteract
  private lateinit var presenter: QrCodePresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.qr_code_layout)
    presenter =
        QrCodePresenter(this, findDefaultWalletInteract, CompositeDisposable(),
            AndroidSchedulers.mainThread())
    presenter.present()
  }

  override fun copyClick(): Observable<Any> {
    return RxView.clicks(copy_button)
  }

  override fun shareClick(): Observable<Any> {
    return RxView.clicks(share_button)
  }

  override fun closeClick(): Observable<Any> {
    return RxView.clicks(close_btn)
  }

  override fun setAddressToClipBoard(walletAddress: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText(
        MyAddressActivity.KEY_ADDRESS, walletAddress)
    if (clipboard != null) {
      clipboard.primaryClip = clip
    }
    Snackbar.make(main_layout, R.string.wallets_address_copied_body, Snackbar.LENGTH_SHORT)
        .show()
  }

  override fun setWalletAddress(walletAddress: String) {
    active_wallet_address.text = walletAddress
  }

  override fun createQrCode(walletAddress: String) {
    val size = Point()
    windowManager.defaultDisplay
        .getSize(size)
    val imageSize = (size.x * QR_IMAGE_WIDTH_RATIO).toInt()
    try {
      val bitMatrix =
          MultiFormatWriter().encode(walletAddress, BarcodeFormat.QR_CODE, imageSize, imageSize,
              null)
      val barcodeEncoder = BarcodeEncoder()
      val bitmapLogo = BitmapFactory.decodeResource(resources, R.drawable.ic_appc_token)
      val qrCode = barcodeEncoder.createBitmap(bitMatrix)
      val mergeQrCode = mergeBitmaps(bitmapLogo, qrCode)
      qr_image.setImageBitmap(mergeQrCode)
    } catch (e: Exception) {
      Snackbar.make(main_layout, getString(R.string.error_fail_generate_qr), Snackbar.LENGTH_SHORT)
          .show()
    }
  }

  override fun showShare(walletAddress: String) {
    ShareCompat.IntentBuilder.from(this)
        .setText(walletAddress)
        .setType("text/plain")
        .setChooserTitle(resources.getString(R.string.referral_share_sheet_title))
        .startChooser()
  }

  override fun onBackPressed() {
    closeSuccess()
    super.onBackPressed()
  }

  override fun onDestroy() {
    presenter.stop()
    super.onDestroy()
  }

  override fun closeSuccess() {
    setResult(Activity.RESULT_OK, Intent())
    finish()
  }

  private fun mergeBitmaps(logo: Bitmap, qrCode: Bitmap): Bitmap {
    val combined = Bitmap.createBitmap(qrCode.width, qrCode.height, qrCode.config)
    val canvas = Canvas(combined)
    canvas.drawBitmap(qrCode, Matrix(), null)

    val resizeLogo = Bitmap.createScaledBitmap(logo, canvas.width / 5, canvas.height / 5, true)
    val centreX = (canvas.width - resizeLogo.width) / 2f
    val centreY = (canvas.height - resizeLogo.height) / 2f
    canvas.drawBitmap(resizeLogo, centreX, centreY, null)
    return combined
  }

  companion object {
    @JvmStatic
    fun newIntent(context: Context): Intent {
      return Intent(context, QrCodeActivity::class.java)
    }

    private const val QR_IMAGE_WIDTH_RATIO = 0.9f
  }
}