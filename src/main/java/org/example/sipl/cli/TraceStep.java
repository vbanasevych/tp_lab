package org.example.sipl.cli;

import java.util.Map;

public record TraceStep(
        int step,
        String statementType,
        String position,
        Map<String, Integer> stateBefore,
        Map<String, Integer> stateAfter
) {}
