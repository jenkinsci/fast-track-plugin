# Fast Track plugin
Jenkins CI plugin that prefers builds in the queue based on there cause and the previous state of the job:
* Builds started by a user have a higher priority than builds started due to an SCM change.
* Builds that have previously failed have a higher priority than builds that previously succeeded.

Note: that builds that have an upstream jobs (i.e. when using the Multi Job plugin) have the highest priority.

## Current state
Still a work in progress. I'll try to add some more documentation (and tests.. and cleaner code..) soon.