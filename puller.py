import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import os
import json
from datetime import datetime

<<<<<<< HEAD
cred = credentials.Certificate('XXXXXX.json')
=======
cred = credentials.Certificate('clubattendancesjam-firebase-adminsdk-yiy90-348810fa7e.json')
>>>>>>> refs/remotes/origin/better
firebase_admin.initialize_app(cred, {
	'databaseURL': 'https://clubattendancesjam.firebaseio.com'
})

# As an admin, the app has access to read and write all data, regradless of Security Rules
ref = db.reference().get()
<<<<<<< HEAD
allFiles = os.listdir("PATH")
=======
desiredPath = "C:\\Users\\advai\\Documents\\GitHub\\AttendanceTracker"
allFiles = os.listdir(desiredPath)
>>>>>>> refs/remotes/origin/better
max_date = 0
min_date = float("inf")
total_backups = 0
for i in allFiles:
	if "CAbackup" in i:
		total_backups += 1
		cur_date = float(i.split("CAbackup")[0].replace("_", "."))  # will be in epoch
		if cur_date > max_date:
			max_date = cur_date
		if cur_date < min_date:
			min_date = cur_date
if total_backups > 5:
<<<<<<< HEAD
	os.remove("PATH" + str(min_date).replace(".", "_") + "CAbackup.json")
=======
	os.remove(desiredPath + "\\" + str(min_date).replace(".", "_") + "CAbackup.json")
>>>>>>> refs/remotes/origin/better
	print("deleted", str(min_date).replace(".", "_") + "CAbackup.json")
with open(str(datetime.now().timestamp()).replace(".", "_") + 'CAbackup.json', 'w+') as outfile:
	json.dump(ref, outfile)
