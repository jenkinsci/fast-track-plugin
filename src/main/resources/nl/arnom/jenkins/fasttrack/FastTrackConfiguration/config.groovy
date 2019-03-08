package nl.arnom.jenkins.fasttrack.FastTrackConfiguration

import lib.FormTagLib


def f = namespace(FormTagLib)
f.section(title: _('Fast Track Queue Sorter')) {
    f.entry(title: _('Rules')) {
        f.repeatableHeteroProperty(
                field: "sortRules",
                oneEach: "true",
                hasHeader: "true",
                addCaption: _("Add another rule")
        )
    }
}