TODO
=====
* Function / User experience
	* Add more Event (suggestions below)
		* GPS position
		* Receiving phone calls
	* Add more Operation (suggestions below)
		* Location setting
		* Perform phone call
	* UI
		* Allow to pick some pre-defined broadcasts or even dynamically search
		* Make EditEventActivity more good-looking
		* Make OutlineFragment more good-looking
		* Use a tree view in EventListFragment
		* Complement 'AboutActivity'
			* Add "Contributors" to the page (or sub-page)
			* If using WebView, remove the "back" FAB; if no longer using WebView, make the "back" FAB go back in the WebView
	* Event
		* Combine Events of the same type to reduce battery consumption (by reducing monitors/`BroadcastReceiver`s)
* Code / Development
	* Draw / Describe the structure of the app for easier understanding
	* All TODO(s) in the code
	* Test coverage
	* Find a way to make Plugin(s) separate apks, rather than narrow them together in the mainframe of Easer
* Issues around
	* Better description of Easer
	* Better documents

To be considered
=======
* Add a 'smart addition of Event' page so that the user can directly select all the needed Event(s) and the app can analyze and add them to the tree(s)
* Change the relationships between Event(s) from tree to graph
