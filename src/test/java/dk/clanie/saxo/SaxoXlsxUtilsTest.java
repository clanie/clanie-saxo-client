package dk.clanie.saxo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import dk.clanie.saxo.dto.SaxoAccountStatementRow;
import dk.clanie.saxo.dto.SaxoBookedTradeAmountsRow;
import dk.clanie.saxo.dto.SaxoTradesExecutedRow;

class SaxoXlsxUtilsTest {

	private final SaxoXlsxUtils utils = new SaxoXlsxUtils();

	private byte[] load(String path) throws IOException {
		return new ClassPathResource(path).getInputStream().readAllBytes();
	}


	// ── AccountStatement ──────────────────────────────────────────────────────

	@Test
	void parseAccountStatement_danish() throws Exception {
		List<SaxoAccountStatementRow> rows = utils.parseAccountStatement(load("saxo-xlsx-samples/da/AccountStatement.xlsx"));
		assertThat(rows).hasSize(126);
		SaxoAccountStatementRow first = rows.get(0);
		assertThat(first.getAccountId()).isEqualTo("128042ASK");
		assertThat(first.getPostingDate()).isEqualTo(LocalDate.of(2026, 4, 27));
	}

	@Test
	void parseAccountStatement_english() throws Exception {
		List<SaxoAccountStatementRow> rows = utils.parseAccountStatement(load("saxo-xlsx-samples/en/AccountStatement.xlsx"));
		assertThat(rows).hasSize(126);
		SaxoAccountStatementRow first = rows.get(0);
		assertThat(first.getAccountId()).isEqualTo("128042ASK");
		assertThat(first.getPostingDate()).isEqualTo(LocalDate.of(2026, 4, 27));
	}


	// ── TradesExecuted ────────────────────────────────────────────────────────

	@Test
	void parseTradesExecuted_danish() throws Exception {
		List<SaxoTradesExecutedRow> rows = utils.parseTradesExecuted(load("saxo-xlsx-samples/da/TradesExecuted.xlsx"));
		assertThat(rows).hasSize(86);
		SaxoTradesExecutedRow first = rows.get(0);
		assertThat(first.getTradeId()).isEqualTo(1646253797L);
		assertThat(first.getAccountId()).isEqualTo("128042ASK");
	}

	@Test
	void parseTradesExecuted_english() throws Exception {
		List<SaxoTradesExecutedRow> rows = utils.parseTradesExecuted(load("saxo-xlsx-samples/en/TradesExecuted.xlsx"));
		assertThat(rows).hasSize(86);
		SaxoTradesExecutedRow first = rows.get(0);
		assertThat(first.getTradeId()).isEqualTo(1646253797L);
		assertThat(first.getAccountId()).isEqualTo("128042ASK");
	}


	// ── BookedTradeAmounts ────────────────────────────────────────────────────

	@Test
	void parseBookedTradeAmounts_danish() throws Exception {
		List<SaxoBookedTradeAmountsRow> rows = utils.parseBookedTradeAmounts(load("saxo-xlsx-samples/da/TradesExecuted.xlsx"));
		assertThat(rows).hasSize(170);
		assertThat(rows.get(0).getRelatedTradeId()).isEqualTo(1645932793L);
	}

	@Test
	void parseBookedTradeAmounts_english() throws Exception {
		List<SaxoBookedTradeAmountsRow> rows = utils.parseBookedTradeAmounts(load("saxo-xlsx-samples/en/TradesExecuted.xlsx"));
		assertThat(rows).hasSize(170);
		assertThat(rows.get(0).getRelatedTradeId()).isEqualTo(1645932793L);
	}


	// ── Validation still rejects unknown sheet names ──────────────────────────

	@Test
	void parseAccountStatement_wrongSheetName_throws() throws Exception {
		// TradesExecuted has a different sheet structure — use it to confirm validation still fires.
		byte[] wrongFile = load("saxo-xlsx-samples/da/TradesExecuted.xlsx");
		assertThatThrownBy(() -> utils.parseAccountStatement(wrongFile))
				.isInstanceOf(RuntimeException.class);
	}


}
