// Initialize MongoDB replica set
try {
    print("Initializing MongoDB replica set...");

    const config = {
        _id: "rs0",
        members: [
            { _id: 0, host: "mongodb1:27017", priority: 1 },
            { _id: 1, host: "mongodb2:27017", priority: 2 },
            { _id: 2, host: "mongodb3:27017", priority: 1 }
        ]
    };

    const status = rs.status();

    if (!status.ok) {
        print("Replica set not initialized. Configuring...");
        rs.initiate(config);

        print("Waiting for primary election...");
        sleep(5000);

        const newStatus = rs.status();
        print("Replica set status: " + JSON.stringify(newStatus, null, 2));

        db = db.getSiblingDB('library_db');

        print("Replica set initialized successfully!");
        print("Primary: " + rs.isMaster().primary);
    } else {
        print("Replica set already initialized");
        print("Current status: " + JSON.stringify(status, null, 2));
    }

} catch (error) {
    print("Error initializing replica set: " + error);
    quit(1);
}