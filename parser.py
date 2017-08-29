import firebase_admin
from firebase_admin import credentials
from firebase_admin import db


def isPaid(ref):
	return "paid" in ref.keys()


cred = credentials.Certificate('XXXXXX.json')
firebase_admin.initialize_app(cred, {
	'databaseURL': 'https://clubattendancesjam.firebaseio.com'
})

# As an admin, the app has access to read and write all data, regradless of Security Rules
ref = db.reference().get()
club_name = input("enter the club name that was set at the beginning: ").lower()
min_percent = float(input("enter the minimum percent to get a credit: "))

master_club_data = ref["club_master"][club_name]
paid = isPaid(master_club_data)
total_meetings = len(master_club_data) - paid
ref.pop("club_master",None)

for i in ref.keys():
	if club_name in ref[i]:
		if paid:
			if isPaid(ref[i][club_name]):
				if round((float(len(ref[i][club_name]) - 1) / float(total_meetings)) * 100, 1) > min_percent:
					print(i, "GRANTED-p%")
				else:
					print(i, "NO-p%")

			else:
				print(i, "NO-pP")
		else:
			if round((float(len(ref[i][club_name]) - 1) / float(total_meetings)) * 100, 1) > min_percent:
				print(i, "GRANTED")
			else:
				print(i, "NO")
