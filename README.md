# Fast Track Queue Optimizer plugin
Optimize the build queue using some simple (configurable) rules.

## How to configure?
Go to **Configure System** in **Manage Jenkins** (`/configure`) and browse to the header **Fast Track Queue Optimizer**.

Here you can add/delete rules and change the order of the configured rules.

When the queue needs to be sorted, each of these rules will be used to compare the pending builds.
As soon as one of the rules establishes a preference for one build over the other, no more rules will be checked.
For example, if you have 3 rules and:
1. First rule has no preference for build A or build B.
2. Second rule prefers build B.
3. Third rule will not be executed and build B will be build before build A.

## Available rules
### Prefer builds started by an upstream build
When comparing jobs, jobs started by an upstream build will be preferred over jobs that do not have this cause.
This might be useful when using _Multi Job_ jobs.

### Prefer builds started by a user
When comparing jobs, a job started from the Jenkins CI web interface by a user will be preferred. Also, jobs that have
been requested by more users will be preferred over jobs that are started by less users.

### Prefer builds of which the estimated duration is less
When comparing jobs, the jobs that historically speaking run faster will be preferred.

### Prefer builds of which the last build has the worst result
When comparing jobs, the result of the previous run will be checked and the build with the worst result will be
preferred. This way failed jobs will be preferred, allowing you to get your build light green again faster.

### Prefer builds that have been waiting for a long time
When comparing jobs, the job that has been in the queue the longest will be preferred.
This rule was added to prevent queue starvation.

It has 2 parameters:
* **Minimum wait time**: jobs that have been waiting for at least this amount of  time (in minutes) will be considered
  by this rule.
* **Minimum difference in time**: rule will only prefer one job over another if the difference in time is at least this
  amount in minutes.

## Extending / adding new rules
All rules must extend the `SortRule` extension point.
Rules can be found in `src/main/java/nl/arnom/jenkins/fasttrack/rules`.
This directory also contains some abstract helper classes.

If you wish to contribute new (or extended) rules, please fork this repository and open up a Pull Request with your changes.