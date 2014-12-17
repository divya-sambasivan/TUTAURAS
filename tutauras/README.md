Tutaurus (Tutor + aurus)
=============================

A dinosaur themed web platform for students to meet tutors. 
Tutors can create subjects and lectures for them.
Further they can view and manage their appointments.

Students can find and book appointments with mentors for the subjects they require.

It is based completely on a SOA based architecture with REST based API's and static pages
at the client.

Package and Deploy
=============================
The application uses Google App Engine with maven and has the following targets:

Deploy to local dev server: mvn appengine:devserver
Stop local dev server cleanly: mvn appengine:devserver_stop
Deploy to remote sever: mvn appengine:update
Repackage on local server (for changes to static files) : mvn package


Appengine API's Used
=============================
1) Blobstore - To store images for subjects (does not work on the remote instance due to blacklisting of google IP's. See https://cloud.google.com/appengine/docs/java/sockets/ for details).
2) Memcache - To cache the results of 'all subjects' call which is repeatedly executed on multiple pages and is common to all users. A call which normally takes about 500ms, takes between 1-4ms.
The caching can explicitly be turned off for calls that require fresh data.
3) TaskQueue - Used to delete descendants when a parent is deleted. Eg: when a subject is deleted, all its lectures and lecture instances need to be deleted. This is not the default behaviour of the datastore and has to be done explicitly. However, it may be time consuming if the parent has a lot of descendants. Hence, async tasks are used to clean up the deletion of an ancestor.
4) Contacts - Used for login and getting information from the current user (instead of having them fill it out again)

Validation
=============================
Before objects are added or updated, their fields are validated. A failed validation will throw a HTTP "BadRequestException".

Testing
=============================
The Rest Assured (https://code.google.com/p/rest-assured/) library is used to test the rest based interfaces.

Libraries Used
=============================
Bootstrap (http://getbootstrap.com/)
hello.js (http://adodson.com/hello.js/)- Leverage google for authentication
bootbox.js (http://bootboxjs.com/) - Twitter Bootstrap overlays for alerts
fullcalendar.js (http://fullcalendar.io/)


