import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import os
import json
from datetime import datetime

cred = credentials.Certificate('XXXXXX.json')
firebase_admin.initialize_app(cred, {
	'databaseURL': 'https://clubattendancesjam.firebaseio.com'
})

# As an admin, the app has access to read and write all data, regradless of Security Rules
ref = db.reference().get()
desiredPath = "C:\\Users\\advai\\Documents\\GitHub\\AttendanceTracker"
allFiles = os.listdir(desiredPath)
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
	os.remove(desiredPath + "\\" + str(min_date).replace(".", "_") + "CAbackup.json")
	print("deleted", str(min_date).replace(".", "_") + "CAbackup.json")
with open(str(datetime.now().timestamp()).replace(".", "_") + 'CAbackup.json', 'w+') as outfile:
	json.dump(ref, outfile)
