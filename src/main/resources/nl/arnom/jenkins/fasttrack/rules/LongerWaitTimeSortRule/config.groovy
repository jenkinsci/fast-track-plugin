package nl.arnom.jenkins.fasttrack.rules.LongerWaitTimeSortRule

import lib.FormTagLib

def f = namespace(FormTagLib)

f.entry(field: "durationMinutes", title: "Minimum wait time (minutes)") {
    f.number()
}

f.entry(field: "deltaMinutes", title: "Minimum difference in time (minutes)") {
    f.number()
}
