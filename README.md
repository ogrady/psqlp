psqlv
=====

Parser for PostgreSQL debug output to visualize the join trees

Howto
====
Assuming your Postgres is compiled with 
	`./configure CPPFLAGS="-DOPTIMIZE_DEBUG -DSELECTIVITY_DEBUG`
and already running.

1. Export the project with the configuration from test/GuiStartup.java
2. Run the jar (the application generates some log itself. You can redirect it to a file in UNIX-fashion if you like with java -jar pgsqlv.jar > mylogfile)
3. Select a logfile via File > Load 
4. Run your query in Postgres
5. The GUI should now fill itself with data. Stop the parser via File > Stop Parsing
6. Inspect the generated plans

#### On the overview (main window):
Each level N holds the considered plans for N-ary-joins. Clicking on one of the plans show you the considered alternatives for the particular plan.
Clicking on a level displays all plans of that level (even those, that are not displayed directly in the level-view).

#### Inside a popup:
Popups display a set of join-trees. Either for one specific plan and its alternatives or for all plans (and their alternatives) on one level.
Clicking on a node in a tree opens another popup that displays the trees from that level (so basically like clicking on a level in the overview).
Doing a left-click on free space around a tree magnifies the whole tree which can come in handy in crowded trees. Right-clicking zooms out again.

#### For example:
Clicking on the button [1,2] in the overview opens a popup with the considered plans for joining the relations 1 and 2 (which is 2-ary).
Then clicking on the node [2] in the popup opens a popup that displays all 1-ary access-strategies the optimizer considered.