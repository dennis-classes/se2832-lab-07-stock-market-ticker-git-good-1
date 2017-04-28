# Analysis

#### Nick Dixon, dixonnj@msoe.edu
#### Ryan Weise, weiserd@msoe.edu

| Original Line Number | Method                            | Defect Description                                                                                                                                                             |
|----------------------|-----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 52                   | none (class variable declaration) | previousQuote is never used. This must be used to implement getChangeSinceLastCheck().                                                                                         |
| 65                   | StockQuoteAnalyzer                | The class Javadoc comments imply that a StockTickerConnectionError can be thrown by the constructor. It cannot, as the constructor does not interact with the stockQuoteSource |
| 76                   | Constructor                       | Not a true defect, but "boolean == true" is redundant.                                                                                                                         |
| 85                   | Constructor                       | AudioPlayer instance is not checked for a possible null value.                                                                                                                 |
| 119                  | playAppropriateAudio              | Happy music should play only in the case that the percent change is greater than 1.                                                                                            |
| 151                  | getPreviousClose                  | Null check for currentQuote is inverted from intended behavior.                                                                                                                |
| 185                  | getChangeSinceClose               | Exception thrown does not match that of the method signature.                                                                                                                  |
| 187                  | getChangeSinceClose               | The return value is calculated incorrectly. This should be a simple accessor method.                                                                                           |
| 204                  | getPercentChangeSinceClose        | The return value is calculated incorrectly, being multiplied by 10000 which is 10 times too large.                                                                             |
| 220                  | getChangeSinceLastCheck           | currentQuote is not checked for a possible null value.                                                                                                                         |
| 220                  | getChangeSinceLastCheck           | The return value is calculated incorrectly; it does not use the previous check in any way.                                                                                     |
