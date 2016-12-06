db = db.getMongo().getDB('admin');
db.createUser({ user: 'admin', pwd: '123456', roles: [ { role: "root", db: "admin" } ]});
db.createUser({ user: 'pep', pwd: 'pep', roles: [ { role: "readWrite", db: "pep" } ] });
