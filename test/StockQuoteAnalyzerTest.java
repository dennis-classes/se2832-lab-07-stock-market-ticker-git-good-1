import exceptions.InvalidAnalysisState;
import exceptions.InvalidStockSymbolException;
import exceptions.StockTickerConnectionError;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class StockQuoteAnalyzerTest {
    @Mock
    private StockQuoteGeneratorInterface generatorMock;
    @Mock
    private StockTickerAudioInterface audioMock;

    private StockQuoteAnalyzer analyzer;
    private final double DELTA = 1.0E-10;

    @BeforeMethod
    public void setUp() throws Exception {
        generatorMock = mock(StockQuoteGeneratorInterface.class);
        audioMock = mock(StockTickerAudioInterface.class);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        generatorMock = null;
        audioMock = null;
    }

    @Test(expectedExceptions = InvalidStockSymbolException.class)
    public void constructorShouldThrowExceptionWhenSymbolIsInvalid() throws Exception {
        analyzer = new StockQuoteAnalyzer("ZZZZZZZZL", generatorMock, audioMock);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void constructorShouldThrowExceptionWhenGeneratorIsNull() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", null, audioMock);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void constructorShouldThrowExceptionWhenAudioPlayerIsNull() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, null);
    }

    @Test(expectedExceptions = InvalidStockSymbolException.class)
    public void constructorShouldThrowInvalidStockSymbolExceptionWhenNoSymbolIsProvided() throws InvalidStockSymbolException {
        analyzer = new StockQuoteAnalyzer("",generatorMock, audioMock);
    }

    @Test
    public void getSymbolShouldReturnSymbolWhenSymbolIsValid() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);

        assertEquals(analyzer.getSymbol(), "AAPL");
    }

    @Test(expectedExceptions = InvalidAnalysisState.class)
    public void getPreviousCloseShouldThrowExceptionWhenNoQuoteHasBeenObtained() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.getPreviousClose();
    }

    @Test
    public void getPreviousCloseShouldReturnCloseWhenValidQuoteIsGiven() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        StockQuote sq = new StockQuote("AAPL", 60.0, 50.0, 0.0);
        when(generatorMock.getCurrentQuote()).thenReturn(sq);
        analyzer.refresh();
        assertEquals(analyzer.getPreviousClose(), 50.0, DELTA);
    }

    @Test
    public void getCurrentPriceShouldReturnTheLastTradeValueWhenValidInputIsSuppliedToAnalyzer() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        StockQuote sq = new StockQuote("AAPL", 50.0,50.0,0.0);
        when(generatorMock.getCurrentQuote()).thenReturn(sq);
        analyzer.refresh();
        assertEquals(analyzer.getCurrentPrice(), 50.0, DELTA);
    }

    @Test
    public void getSymbolShouldReturnTheLastTradeValueWhenValidInputIsSuppliedToAnalyzer() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        StockQuote sq = new StockQuote("AAPL", 50.0,50.0,0.0);
        when(generatorMock.getCurrentQuote()).thenReturn(sq);
        analyzer.refresh();
        assertEquals(analyzer.getSymbol(), "AAPL");
    }

    @Test
    public void playAppropriateAudioShouldPlaySadMusicWhenPercentChangeIsLessThanNegative1() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        StockQuote sq = new StockQuote("AAPL", 50.0,25.0,-25.0);
        when(generatorMock.getCurrentQuote()).thenReturn(sq);
        analyzer.refresh();
        analyzer.playAppropriateAudio();
        verify(audioMock, times(1)).playSadMusic();
    }

    @Test
    public void playAppropriateAudioShouldPlayHappyMusicWhenPercentChangeIsGreaterThan0() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        StockQuote sq = new StockQuote("AAPL", 25.0,50.0,25.0);
        when(generatorMock.getCurrentQuote()).thenReturn(sq);
        analyzer.refresh();
        analyzer.playAppropriateAudio();
        verify(audioMock, times(1)).playHappyMusic();
    }
    @Test
    public void playAppropriateAudioShouldPlayErrorMusicWhenNoQouteIsPresent() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.refresh();
        analyzer.playAppropriateAudio();
        verify(audioMock, times(1)).playErrorMusic();
    }

    @Test
    public void getChangeSinceCloseShouldReturnChangeWhenValid() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("AAPL", 40.0, 35.0, -5.0));
        analyzer.refresh();
        assertEquals(analyzer.getChangeSinceClose(), -5.0);
    }
}