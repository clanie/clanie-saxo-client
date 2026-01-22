package dk.clanie.saxo.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

class SaxoAccountTest {


	@Test
	void testDeserialization() throws Exception {
		JsonMapper objectMapper = JsonMapper.builder().build();
		
		String json = """
				[ {
				"AccountGroupKey" : "YmxhLWJsYQ==",
				"AccountGroupName" : "Aktiesparekonto",
				"AccountId" : "111111ASK",
				"AccountKey" : "YmxhLWJsYQ==",
				"AccountSubType" : "None",
				"AccountType" : "TaxFavoredAccount",
				"AccountValueProtectionLimit" : 0.0,
				"Active" : true,
				"CanUseCashPositionsAsMarginCollateral" : false,
				"CfdBorrowingCostsActive" : false,
				"ClientId" : "11111111",
				"ClientKey" : "aaaaaaa|YmxhLWJsYQ==",
				"CreationDate" : "2024-01-01T01:01:01.000Z",
				"Currency" : "DKK",
				"CurrencyDecimals" : 2,
				"DirectMarketAccess" : false,
				"DisplayName" : "Aktiesparekonto",
				"ExternalReference" : "",
				"FractionalOrderEnabled" : false,
				"FractionalOrderEnabledAssetTypes" : [ ],
				"IndividualMargining" : true,
				"IsCurrencyConversionAtSettlementTime" : false,
				"IsMarginTradingAllowed" : false,
				"IsShareable" : false,
				"IsTrialAccount" : false,
				"LegalAssetTypes" : [ "Stock", "Cash", "MutualFund", "Etf", "Etc", "Etn", "Fund", "Rights", "IpoOnStock", "CompanyWarrant", "StockIndex" ],
				"ManagementType" : "Client",
				"MarginCalculationMethod" : "Default",
				"MarginLendingEnabled" : "None",
				"PortfolioBasedMarginEnabled" : false,
				"Sharing" : [ "NoSharing" ],
				"SupportsAccountValueProtectionLimit" : true,
				"UseCashPositionsAsMarginCollateral" : true,
				"currencyConversionAtSettlementTime" : false,
				"marginTradingAllowed" : false,
				"shareable" : false,
				"trialAccount" : false
				}, {
				"AccountGroupKey" : "YmxhLWJsYQ==",
				"AccountGroupName" : null,
				"AccountId" : "222222INET",
				"AccountKey" : "YmxhLWJsYQ==",
				"AccountSubType" : "None",
				"AccountType" : "Normal",
				"AccountValueProtectionLimit" : 0.0,
				"Active" : true,
				"CanUseCashPositionsAsMarginCollateral" : true,
				"CfdBorrowingCostsActive" : true,
				"ClientId" : "11111111",
				"ClientKey" : "YmxhLWJsYQ==",
				"CreationDate" : "2021-01-01T01:01:00.000Z",
				"Currency" : "DKK",
				"CurrencyDecimals" : 2,
				"DirectMarketAccess" : false,
				"DisplayName" : null,
				"ExternalReference" : "",
				"FractionalOrderEnabled" : false,
				"FractionalOrderEnabledAssetTypes" : [ ],
				"IndividualMargining" : false,
				"IsCurrencyConversionAtSettlementTime" : false,
				"IsMarginTradingAllowed" : true,
				"IsShareable" : false,
				"IsTrialAccount" : false,
				"LegalAssetTypes" : [ "FxSpot", "FxForwards", "FxVanillaOption", "ContractFutures", "FuturesStrategy", "Stock", "StockOption", "Bond", "FuturesOption", "StockIndexOption", "Cash", "CfdOnStock", "CfdOnIndex", "CfdOnFutures", "StockIndex", "MutualFund", "CfdOnEtf", "CfdOnEtc", "CfdOnEtn", "CfdOnFund", "CfdOnRights", "CfdOnCompanyWarrant", "Etf", "Etc", "Etn", "Fund", "FxSwap", "Rights", "IpoOnStock", "CompanyWarrant" ],
				"ManagementType" : "Client",
				"MarginCalculationMethod" : "Default",
				"MarginLendingEnabled" : "None",
				"PortfolioBasedMarginEnabled" : false,
				"Sharing" : [ "NoSharing" ],
				"SupportsAccountValueProtectionLimit" : false,
				"UseCashPositionsAsMarginCollateral" : true,
				"currencyConversionAtSettlementTime" : false,
				"marginTradingAllowed" : true,
				"shareable" : false,
				"trialAccount" : false
				}, {
				"AccountGroupKey" : "YmxhLWJsYQ==",
				"AccountGroupName" : null,
				"AccountId" : "333333INET",
				"AccountKey" : "YmxhLWJsYQ==",
				"AccountSubType" : "None",
				"AccountType" : "Normal",
				"AccountValueProtectionLimit" : 0.0,
				"Active" : true,
				"CanUseCashPositionsAsMarginCollateral" : true,
				"CfdBorrowingCostsActive" : true,
				"ClientId" : "11111111",
				"ClientKey" : "YmxhLWJsYQ==",
				"CreationDate" : "2021-01-01T01:01:01.000Z",
				"Currency" : "EUR",
				"CurrencyDecimals" : 2,
				"DirectMarketAccess" : false,
				"DisplayName" : null,
				"ExternalReference" : "",
				"FractionalOrderEnabled" : false,
				"FractionalOrderEnabledAssetTypes" : [ ],
				"IndividualMargining" : false,
				"IsCurrencyConversionAtSettlementTime" : false,
				"IsMarginTradingAllowed" : true,
				"IsShareable" : false,
				"IsTrialAccount" : false,
				"LegalAssetTypes" : [ "FxSpot", "FxForwards", "FxVanillaOption", "ContractFutures", "FuturesStrategy", "Stock", "StockOption", "Bond", "FuturesOption", "StockIndexOption", "Cash", "CfdOnStock", "CfdOnIndex", "CfdOnFutures", "StockIndex", "MutualFund", "CfdOnEtf", "CfdOnEtc", "CfdOnEtn", "CfdOnFund", "CfdOnRights", "CfdOnCompanyWarrant", "Etf", "Etc", "Etn", "Fund", "FxSwap", "Rights", "IpoOnStock", "CompanyWarrant" ],
				"ManagementType" : "Client",
				"MarginCalculationMethod" : "Default",
				"MarginLendingEnabled" : "None",
				"PortfolioBasedMarginEnabled" : false,
				"Sharing" : [ "NoSharing" ],
				"SupportsAccountValueProtectionLimit" : false,
				"UseCashPositionsAsMarginCollateral" : true,
				"currencyConversionAtSettlementTime" : false,
				"marginTradingAllowed" : true,
				"shareable" : false,
				"trialAccount" : false
				}, {
				"AccountGroupKey" : "YmxhLWJsYQ==",
				"AccountGroupName" : null,
				"AccountId" : "444444INET",
				"AccountKey" : "YmxhLWJsYQ==",
				"AccountSubType" : "None",
				"AccountType" : "Normal",
				"AccountValueProtectionLimit" : 0.0,
				"Active" : true,
				"CanUseCashPositionsAsMarginCollateral" : true,
				"CfdBorrowingCostsActive" : true,
				"ClientId" : "11111111",
				"ClientKey" : "YmxhLWJsYQ==",
				"CreationDate" : "2021-01-01T01:01:00.000Z",
				"Currency" : "USD",
				"CurrencyDecimals" : 2,
				"DirectMarketAccess" : false,
				"DisplayName" : null,
				"ExternalReference" : "",
				"FractionalOrderEnabled" : false,
				"FractionalOrderEnabledAssetTypes" : [ ],
				"IndividualMargining" : false,
				"IsCurrencyConversionAtSettlementTime" : false,
				"IsMarginTradingAllowed" : true,
				"IsShareable" : false,
				"IsTrialAccount" : false,
				"LegalAssetTypes" : [ "FxSpot", "FxForwards", "FxVanillaOption", "ContractFutures", "FuturesStrategy", "Stock", "StockOption", "Bond", "FuturesOption", "StockIndexOption", "Cash", "CfdOnStock", "CfdOnIndex", "CfdOnFutures", "StockIndex", "MutualFund", "CfdOnEtf", "CfdOnEtc", "CfdOnEtn", "CfdOnFund", "CfdOnRights", "CfdOnCompanyWarrant", "Etf", "Etc", "Etn", "Fund", "FxSwap", "Rights", "IpoOnStock", "CompanyWarrant" ],
				"ManagementType" : "Client",
				"MarginCalculationMethod" : "Default",
				"MarginLendingEnabled" : "None",
				"PortfolioBasedMarginEnabled" : false,
				"Sharing" : [ "NoSharing" ],
				"SupportsAccountValueProtectionLimit" : false,
				"UseCashPositionsAsMarginCollateral" : true,
				"currencyConversionAtSettlementTime" : false,
				"marginTradingAllowed" : true,
				"shareable" : false,
				"trialAccount" : false
				}, {
				"AccountGroupKey" : "YmxhLWJsYQ==",
				"AccountGroupName" : null,
				"AccountId" : "555555INET",
				"AccountKey" : "YmxhLWJsYQ==",
				"AccountSubType" : "None",
				"AccountType" : "Normal",
				"AccountValueProtectionLimit" : 0.0,
				"Active" : true,
				"CanUseCashPositionsAsMarginCollateral" : true,
				"CfdBorrowingCostsActive" : true,
				"ClientId" : "11111111",
				"ClientKey" : "YmxhLWJsYQ==",
				"CreationDate" : "2021-01-01T01:01:00.000Z",
				"Currency" : "CAD",
				"CurrencyDecimals" : 2,
				"DirectMarketAccess" : false,
				"DisplayName" : null,
				"ExternalReference" : "",
				"FractionalOrderEnabled" : false,
				"FractionalOrderEnabledAssetTypes" : [ ],
				"IndividualMargining" : false,
				"IsCurrencyConversionAtSettlementTime" : false,
				"IsMarginTradingAllowed" : true,
				"IsShareable" : false,
				"IsTrialAccount" : false,
				"LegalAssetTypes" : [ "FxSpot", "FxForwards", "FxVanillaOption", "ContractFutures", "FuturesStrategy", "Stock", "StockOption", "Bond", "FuturesOption", "StockIndexOption", "Cash", "CfdOnStock", "CfdOnIndex", "CfdOnFutures", "StockIndex", "MutualFund", "CfdOnEtf", "CfdOnEtc", "CfdOnEtn", "CfdOnFund", "CfdOnRights", "CfdOnCompanyWarrant", "Etf", "Etc", "Etn", "Fund", "FxSwap", "Rights", "IpoOnStock", "CompanyWarrant" ],
				"ManagementType" : "Client",
				"MarginCalculationMethod" : "Default",
				"MarginLendingEnabled" : "None",
				"PortfolioBasedMarginEnabled" : false,
				"Sharing" : [ "NoSharing" ],
				"SupportsAccountValueProtectionLimit" : false,
				"UseCashPositionsAsMarginCollateral" : true,
				"currencyConversionAtSettlementTime" : false,
				"marginTradingAllowed" : true,
				"shareable" : false,
				"trialAccount" : false
				} ]
				""";
		
		SaxoAccount[] accounts = objectMapper.readValue(json, SaxoAccount[].class);
		
		assertThat(accounts).hasSize(5);
		
		// Validate first account (Aktiesparekonto)
		SaxoAccount account1 = accounts[0];
		assertThat(account1.getAccountGroupKey()).isEqualTo("YmxhLWJsYQ==");
		assertThat(account1.getAccountGroupName()).isEqualTo("Aktiesparekonto");
		assertThat(account1.getAccountId()).isEqualTo("111111ASK");
		assertThat(account1.getAccountKey()).isEqualTo("YmxhLWJsYQ==");
		assertThat(account1.getAccountSubType()).isEqualTo(SaxoAccountSubType.None);
		assertThat(account1.getAccountType()).isEqualTo(SaxoAccountType.TaxFavoredAccount);
		assertThat(account1.getAccountValueProtectionLimit()).isEqualTo(0.0);
		assertThat(account1.isActive()).isTrue();
		assertThat(account1.isCanUseCashPositionsAsMarginCollateral()).isFalse();
		assertThat(account1.isCfdBorrowingCostsActive()).isFalse();
		assertThat(account1.getClientId()).isEqualTo("11111111");
		assertThat(account1.getClientKey()).isEqualTo("aaaaaaa|YmxhLWJsYQ==");
		assertThat(account1.getCreationDate()).isEqualTo(Instant.parse("2024-01-01T01:01:01.000Z"));
		assertThat(account1.getCurrency()).isEqualTo(SaxoCurrencyCode.DKK);
		assertThat(account1.getCurrencyDecimals()).isEqualTo(2);
		assertThat(account1.isDirectMarketAccess()).isFalse();
		assertThat(account1.getDisplayName()).isEqualTo("Aktiesparekonto");
		assertThat(account1.getExternalReference()).isEmpty();
		assertThat(account1.isFractionalOrderEnabled()).isFalse();
		assertThat(account1.getFractionalOrderEnabledAssetTypes()).isEmpty();
		assertThat(account1.isIndividualMargining()).isTrue();
		assertThat(account1.isCurrencyConversionAtSettlementTime()).isFalse();
		assertThat(account1.isMarginTradingAllowed()).isFalse();
		assertThat(account1.isShareable()).isFalse();
		assertThat(account1.isTrialAccount()).isFalse();
		assertThat(account1.getLegalAssetTypes()).containsExactly(
			SaxoAssetType.Stock,
			SaxoAssetType.Cash,
			SaxoAssetType.MutualFund,
			SaxoAssetType.Etf,
			SaxoAssetType.Etc,
			SaxoAssetType.Etn,
			SaxoAssetType.Fund,
			SaxoAssetType.Rights,
			SaxoAssetType.IpoOnStock,
			SaxoAssetType.CompanyWarrant,
			SaxoAssetType.StockIndex
		);
		assertThat(account1.getManagementType()).isEqualTo("Client");
		assertThat(account1.getMarginCalculationMethod()).isEqualTo("Default");
		assertThat(account1.getMarginLendingEnabled()).isEqualTo("None");
		assertThat(account1.isPortfolioBasedMarginEnabled()).isFalse();
		assertThat(account1.getSharing()).containsExactly("NoSharing");
		assertThat(account1.isSupportsAccountValueProtectionLimit()).isTrue();
		assertThat(account1.isUseCashPositionsAsMarginCollateral()).isTrue();
		
		// Validate second account (DKK Normal account)
		SaxoAccount account2 = accounts[1];
		assertThat(account2.getAccountGroupName()).isNull();
		assertThat(account2.getAccountId()).isEqualTo("222222INET");
		assertThat(account2.getAccountType()).isEqualTo(SaxoAccountType.Normal);
		assertThat(account2.getClientKey()).isEqualTo("YmxhLWJsYQ==");
		assertThat(account2.getCreationDate()).isEqualTo(Instant.parse("2021-01-01T01:01:00.000Z"));
		assertThat(account2.getCurrency()).isEqualTo(SaxoCurrencyCode.DKK);
		assertThat(account2.getDisplayName()).isNull();
		assertThat(account2.isCanUseCashPositionsAsMarginCollateral()).isTrue();
		assertThat(account2.isCfdBorrowingCostsActive()).isTrue();
		assertThat(account2.isMarginTradingAllowed()).isTrue();
		assertThat(account2.getLegalAssetTypes()).hasSize(30);
		assertThat(account2.getLegalAssetTypes()).contains(
			SaxoAssetType.FxSpot,
			SaxoAssetType.ContractFutures,
			SaxoAssetType.Stock,
			SaxoAssetType.CfdOnStock
		);
		
		// Validate third account (EUR)
		SaxoAccount account3 = accounts[2];
		assertThat(account3.getAccountId()).isEqualTo("333333INET");
		assertThat(account3.getCurrency()).isEqualTo(SaxoCurrencyCode.EUR);
		
		// Validate fourth account (USD)
		SaxoAccount account4 = accounts[3];
		assertThat(account4.getAccountId()).isEqualTo("444444INET");
		assertThat(account4.getCurrency()).isEqualTo(SaxoCurrencyCode.USD);
		
		// Validate fifth account (CAD)
		SaxoAccount account5 = accounts[4];
		assertThat(account5.getAccountId()).isEqualTo("555555INET");
		assertThat(account5.getCurrency()).isEqualTo(SaxoCurrencyCode.CAD);
	}


}
