package com.oleksandrpriadko

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter

class Kermit(isDebug: Boolean) : Logger(
    config = loggerConfigInit(
        platformLogWriter(),
        minSeverity = if (isDebug) Severity.Verbose else Severity.Assert
    )
)

lateinit var kermit: Kermit