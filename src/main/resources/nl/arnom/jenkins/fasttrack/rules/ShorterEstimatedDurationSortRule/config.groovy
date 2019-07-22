package nl.arnom.jenkins.fasttrack.rules.ShorterEstimatedDurationSortRule

import lib.FormTagLib

def f = namespace(FormTagLib)

f.entry(field: "minimumDeltaSeconds", title: "Minimum difference in estimated duration (seconds)") {
    f.number()
}