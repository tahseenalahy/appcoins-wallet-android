package com.asfoundation.wallet.ui.iab;

import android.os.Bundle;
import com.asfoundation.wallet.billing.adyen.PaymentType;
import com.asfoundation.wallet.entity.TransactionBuilder;
import java.math.BigDecimal;
import org.jetbrains.annotations.NotNull;

/**
 * Created by franciscocalado on 20/07/2018.
 */

public interface IabView {

  void disableBack();

  void enableBack();

  void finish(Bundle data);

  void showError();

  void close(Bundle bundle);

  void navigateToWebViewAuthorization(String url);

  void showOnChain(BigDecimal amount, boolean isBds, String bonus);

  void showAdyenPayment(BigDecimal amount, String currency, boolean isBds, PaymentType paymentType,
      String bonus);

  void showAppcoinsCreditsPayment(BigDecimal amount);

  void showLocalPayment(String domain, String skuId, String originalAmount, String currency,
      String bonus, String selectedPaymentMethod, String developerAddress, String type,
      BigDecimal amount, String callbackUrl, String orderReference, String payload);

  void showPaymentMethodsView();

  void showPaymentMethodsView(PaymentMethodsView.SelectedPaymentMethod preSelectedMethod);

  void showShareLinkPayment(String domain, String skuId, String originalAmount,
      String originalCurrency, BigDecimal amount, @NotNull String type,
      String selectedPaymentMethod);

  void showMergedAppcoins(TransactionBuilder transaction, BigDecimal fiatAmount, String currency,
      String bonus, boolean appcEnabled, boolean creditsEnabled);
}
