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

    @Test(expectedExceptions = InvalidStockSymbolException.class)
    public void constructorShouldThrowInvalidStockSymbolExceptionWhenNoSymbolIsProvided() throws InvalidStockSymbolException {
        analyzer = new StockQuoteAnalyzer("",generatorMock, audioMock);
    }

    @Test
    public void constructorShouldWorkWhenValidSymbolsAreProvided() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        StockQuote sq = new StockQuote("AAPL", 50.0,50.0,0.0);
        when(generatorMock.getCurrentQuote()).thenReturn(sq);
        analyzer.refresh();
        assertEquals(analyzer.getCurrentPrice(), 50.0);
    }

}