db = db.getSiblingDB('EmployeeService');

db.Employee.insertMany([
  // Employee 1: US Citizen
  {
    _id: "emp001",
    userId: "3",
    firstName: "Alice",
    lastName: "Smith",
    email: "alice.smith@example.com",
    middleName: "P.",
    preferredName: "Ali",
    avatarPath: "/avatars/alice.jpg",
    cellPhone: "555-111-2222",
    workPhone: "555-333-4444",
    gender: "Female",
    ssn: "999-88-7777",
    dob: new Date("1990-05-15T00:00:00Z"),
    startDate: new Date("2020-01-01T00:00:00Z"),
    houseId: "house_xyz_789",
    addresses: [
      {
        id: "addr001",
        type: "PRIMARY",
        addressLine1: "123 Main St",
        city: "Anytown",
        state: "CA",
        zipCode: "90210"
      }
    ],
    workAuthorization: {
      isUsCitizen: true,
      greenCardHolder: false,
      type: "N/A" // US Citizen
    },
    driverLicense: {
      hasLicense: true,
      licenseNumber: "DL1234567",
      expirationDate: new Date("2028-12-31T00:00:00Z")
    },
    emergencyContacts: [
      {
        id: "emc001",
        firstName: "Bob",
        lastName: "Smith",
        relationship: "Spouse",
        cellPhone: "555-555-6666",
        email: "bob.smith@example.com"
      }
    ],
    applicationType: "ONBOARD"
  },
  // Employee 2: Green Card Holder
  {
    _id: "emp002",
    userId: "4",
    firstName: "Bob",
    lastName: "Johnson",
    email: "bob.johnson@example.com",
    gender: "Male",
    dob: new Date("1985-11-22T00:00:00Z"),
    startDate: new Date("2022-03-10T00:00:00Z"),
    addresses: [
      {
        id: "addr002",
        type: "PRIMARY",
        addressLine1: "789 Pine Rd",
        city: "Smallville",
        state: "TX",
        zipCode: "75001"
      }
    ],
    workAuthorization: {
      isUsCitizen: false,
      greenCardHolder: true,
      type: "N/A" // Green Card Holder
    },
    emergencyContacts: [
      {
        id: "emc002",
        firstName: "Diana",
        lastName: "Johnson",
        relationship: "Mother",
        cellPhone: "555-999-0000"
      }
    ],
    applicationType: "ONBOARD"
  },
  // Employee 3: H1B Work Authorization
  {
    _id: "emp003",
    userId: "5",
    firstName: "Carol",
    lastName: "Davis",
    email: "carol.davis@example.com",
    gender: "Female",
    dob: new Date("1992-07-01T00:00:00Z"),
    startDate: new Date("2023-01-15T00:00:00Z"),
    addresses: [
      {
        id: "addr003",
        type: "PRIMARY",
        addressLine1: "101 Maple Lane",
        city: "Big City",
        state: "IL",
        zipCode: "60601"
      }
    ],
    workAuthorization: {
      isUsCitizen: false,
      greenCardHolder: false,
      type: "H1B",
      startDate: new Date("2023-01-15T00:00:00Z"),
      endDate: new Date("2026-01-14T00:00:00Z"),
      lastModificationDate: new Date("2023-01-10T00:00:00Z")
    },
    applicationType: "ONBOARD"
  },
  // Employee 4: L2 Work Authorization
  {
    _id: "emp004",
    userId: "6",
    firstName: "David",
    lastName: "Miller",
    email: "david.miller@example.com",
    gender: "Male",
    dob: new Date("1988-03-20T00:00:00Z"),
    startDate: new Date("2021-09-01T00:00:00Z"),
    addresses: [
      {
        id: "addr004",
        type: "PRIMARY",
        addressLine1: "202 Elm Street",
        city: "Tech Town",
        state: "WA",
        zipCode: "98101"
      }
    ],
    workAuthorization: {
      isUsCitizen: false,
      greenCardHolder: false,
      type: "L2",
      startDate: new Date("2021-09-01T00:00:00Z"),
      endDate: new Date("2025-08-31T00:00:00Z"),
      lastModificationDate: new Date("2021-08-25T00:00:00Z")
    },
    applicationType: "ONBOARD"
  },
  // Employee 5: F1 Work Authorization (e.g., CPT/OPT)
  {
    _id: "emp005",
    userId: "7",
    firstName: "Eve",
    lastName: "White",
    email: "eve.white@example.com",
    gender: "Female",
    dob: new Date("1995-09-10T00:00:00Z"),
    startDate: new Date("2024-06-01T00:00:00Z"),
    addresses: [
      {
        id: "addr005",
        type: "PRIMARY",
        addressLine1: "303 College Ave",
        city: "University City",
        state: "MA",
        zipCode: "02139"
      }
    ],
    workAuthorization: {
      isUsCitizen: false,
      greenCardHolder: false,
      type: "F1", // Represents F1 visa, often used for CPT/OPT
      startDate: new Date("2024-06-01T00:00:00Z"),
      endDate: new Date("2025-05-31T00:00:00Z"),
      lastModificationDate: new Date("2024-05-28T00:00:00Z")
    },
    applicationType: "ONBOARD"
  }
]);

print("Fake data inserted into Employee collection successfully!");