package com.asfoundation.wallet.di;

import com.asfoundation.wallet.entity.NetworkInfo;
import com.asfoundation.wallet.interact.DefaultTokenProvider;
import com.asfoundation.wallet.interact.FetchTransactionsInteract;
import com.asfoundation.wallet.interact.FindDefaultNetworkInteract;
import com.asfoundation.wallet.interact.FindDefaultWalletInteract;
import com.asfoundation.wallet.repository.OffChainTransactions;
import com.asfoundation.wallet.repository.TokenLocalSource;
import com.asfoundation.wallet.repository.TokenRepository;
import com.asfoundation.wallet.repository.TransactionLocalSource;
import com.asfoundation.wallet.repository.TransactionRepositoryType;
import com.asfoundation.wallet.repository.WalletRepositoryType;
import com.asfoundation.wallet.repository.Web3jProvider;
import com.asfoundation.wallet.router.AirdropRouter;
import com.asfoundation.wallet.router.BalanceRouter;
import com.asfoundation.wallet.router.ExternalBrowserRouter;
import com.asfoundation.wallet.router.ManageWalletsRouter;
import com.asfoundation.wallet.router.MyAddressRouter;
import com.asfoundation.wallet.router.RewardsLevelRouter;
import com.asfoundation.wallet.router.SendRouter;
import com.asfoundation.wallet.router.SettingsRouter;
import com.asfoundation.wallet.router.TopUpRouter;
import com.asfoundation.wallet.router.TransactionDetailRouter;
import com.asfoundation.wallet.service.TickerService;
import com.asfoundation.wallet.service.TokenExplorerClientType;
import com.asfoundation.wallet.transactions.TransactionsAnalytics;
import com.asfoundation.wallet.transactions.TransactionsMapper;
import com.asfoundation.wallet.ui.AppcoinsApps;
import com.asfoundation.wallet.ui.balance.BalanceInteract;
import com.asfoundation.wallet.ui.gamification.GamificationInteractor;
import com.asfoundation.wallet.viewmodel.TransactionsViewModelFactory;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module class TransactionsModule {

  @Provides TransactionsViewModelFactory provideTransactionsViewModelFactory(
      FindDefaultNetworkInteract findDefaultNetworkInteract,
      FindDefaultWalletInteract findDefaultWalletInteract,
      FetchTransactionsInteract fetchTransactionsInteract, SettingsRouter settingsRouter,
      SendRouter sendRouter, TransactionDetailRouter transactionDetailRouter,
      MyAddressRouter myAddressRouter, BalanceRouter balanceRouter,
      ExternalBrowserRouter externalBrowserRouter, DefaultTokenProvider defaultTokenProvider,
      TransactionsMapper transactionsMapper, AppcoinsApps applications,
      OffChainTransactions offChainTransactions, RewardsLevelRouter rewardsLevelRouter,
      GamificationInteractor gamificationInteractor, TopUpRouter topUpRouter,
      TransactionsAnalytics analytics, BalanceInteract balanceInteract) {
    return new TransactionsViewModelFactory(findDefaultNetworkInteract, findDefaultWalletInteract,
        fetchTransactionsInteract, settingsRouter, sendRouter, transactionDetailRouter,
        myAddressRouter, balanceRouter, externalBrowserRouter, defaultTokenProvider,
        transactionsMapper, applications, offChainTransactions, rewardsLevelRouter,
        gamificationInteractor, topUpRouter, analytics, balanceInteract);
  }

  @Provides FetchTransactionsInteract provideFetchTransactionsInteract(
      TransactionRepositoryType transactionRepository) {
    return new FetchTransactionsInteract(transactionRepository);
  }

  @Provides ManageWalletsRouter provideManageWalletsRouter() {
    return new ManageWalletsRouter();
  }

  @Provides SettingsRouter provideSettingsRouter() {
    return new SettingsRouter();
  }

  @Provides SendRouter provideSendRouter() {
    return new SendRouter();
  }

  @Provides TopUpRouter provideSendRouterTopUpRouter() {
    return new TopUpRouter();
  }

  @Provides TransactionDetailRouter provideTransactionDetailRouter() {
    return new TransactionDetailRouter();
  }

  @Provides MyAddressRouter provideMyAddressRouter() {
    return new MyAddressRouter();
  }

  @Provides BalanceRouter provideMyTokensRouter() {
    return new BalanceRouter();
  }

  @Provides ExternalBrowserRouter provideExternalBrowserRouter() {
    return new ExternalBrowserRouter();
  }

  @Singleton @Provides TokenRepository provideTokenRepository(WalletRepositoryType walletRepository,
      TokenExplorerClientType tokenExplorerClientType, TokenLocalSource tokenLocalSource,
      TransactionLocalSource inDiskCache, TickerService tickerService, Web3jProvider web3j,
      NetworkInfo networkInfo, DefaultTokenProvider defaultTokenProvider) {
    return new TokenRepository(walletRepository, tokenExplorerClientType, tokenLocalSource,
        inDiskCache, tickerService, web3j, networkInfo, defaultTokenProvider);
  }

  @Provides AirdropRouter provideAirdropRouter() {
    return new AirdropRouter();
  }

  @Provides RewardsLevelRouter providerRewardsLevelRouter() {
    return new RewardsLevelRouter();
  }
}
