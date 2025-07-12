db = db.getSiblingDB('EmployeeService');

db.createCollection("Employee", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["userId", "firstName", "lastName", "email"],
      properties: {
        userId: {
          bsonType: "string",
          description: "Employee ID - required"
        },
        firstName: {
          bsonType: "string",
          description: "Employee first name - required"
        },
        lastName: {
          bsonType: "string",
          description: "Employee last name - required"
        },
        middleName: {
          bsonType: "string",
          description: "Employee middle name - optional"
        },
        preferredName: {
          bsonType: "string",
          description: "Employee preferred name - optional"
        },
        avatarPath: {
          bsonType: "string",
          description: "Path to employee's avatar picture - optional"
        },
        email: {
          bsonType: "string",
          description: "Employee email - required"
        },
        cellPhone: {
          bsonType: "string",
          description: "Employee cell phone - optional"
        },
        workPhone: {
          bsonType: "string",
          description: "Employee work phone - optional"
        },
        gender: {
          bsonType: "string",
          description: "Employee gender - optional"
        },
        ssn: {
          bsonType: "string",
          description: "Employee SSN - optional"
        },
        dob: {
          bsonType: "date",
          description: "Employee date of birth - optional"
        },
        startDate: {
          bsonType: "date",
          description: "Employee start date - optional"
        },
        endDate: {
          bsonType: "date",
          description: "Employee end date - optional"
        },
        houseId: {
          bsonType: "string",
          description: "Reference to House ID - optional"
        },
        addresses: {
          bsonType: "array",
          description: "Array of employee's primary and secondary addresses",
          items: {
            bsonType: "object",
            required: ["type", "addressLine1", "city", "state", "zipCode"],
            properties: {
              id: { bsonType: "string" },
              type: {
                bsonType: "string",
                enum: ["PRIMARY", "SECONDARY"],
                description: "Type of address (PRIMARY or SECONDARY)"
              },
              addressLine1: { bsonType: "string" },
              addressLine2: { bsonType: "string" },
              city: { bsonType: "string" },
              state: { bsonType: "string" },
              zipCode: { bsonType: "string" }
            }
          }
        },
        workAuthorization: {
          bsonType: "object",
          description: "Employee's work authorization details - optional",
          properties: {
            isUsCitizen: {
              bsonType: "bool",
              description: "True if US Citizen"
            },
            greenCardHolder: {
              bsonType: "bool",
              description: "True if Green Card Holder"
            },
            type: {
              bsonType: "string",
              enum: ["H1B", "L2", "F1", "H4", "OTHER", "N/A"],
              description: "Type of work authorization (e.g., H1B, L2, F1, H4, OTHER)"
            },
            startDate: {
              bsonType: "date",
              description: "Work authorization start date"
            },
            endDate: {
              bsonType: "date",
              description: "Work authorization end date"
            },
            lastModificationDate: {
              bsonType: "date",
              description: "Last modification date of work authorization status"
            }
          }
        },
        driverLicense: {
          bsonType: "object",
          description: "Employee's driver license information - optional",
          properties: {
            hasLicense: {
              bsonType: "bool",
              description: "True if employee has a driver's license"
            },
            licenseNumber: {
              bsonType: "string",
              description: "Driver license number"
            },
            expirationDate: {
              bsonType: "date",
              description: "Driver license expiration date"
            }
          }
        },
        emergencyContacts: {
          bsonType: "array",
          description: "Array of emergency contact information",
          items: {
            bsonType: "object",
            required: ["firstName", "lastName", "relationship"],
            properties: {
              id: { bsonType: "string" },
              firstName: { bsonType: "string" },
              lastName: { bsonType: "string" },
              middleName: { bsonType: "string" },
              cellPhone: { bsonType: "string" },
              alternatePhone: { bsonType: "string" },
              email: { bsonType: "string" },
              relationship: { bsonType: "string" },
              address: {
                bsonType: "object",
                description: "Address of the emergency contact - optional",
                properties: {
                  addressLine1: { bsonType: "string" },
                  addressLine2: { bsonType: "string" },
                  city: { bsonType: "string" },
                  state: { bsonType: "string" },
                  zipCode: { bsonType: "string" }
                }
              }
            }
          }
        },
        references: {
          bsonType: "array",
          description: "Array of professional/personal references",
          items: {
            bsonType: "object",
            required: ["firstName", "lastName", "relationship"],
            properties: {
              id: { bsonType: "string" },
              firstName: { bsonType: "string" },
              lastName: { bsonType: "string" },
              middleName: { bsonType: "string" },
              phone: { bsonType: "string" },
              email: { bsonType: "string" },
              relationship: { bsonType: "string" },
              address: {
                bsonType: "object",
                description: "Address of the reference - optional",
                properties: {
                  addressLine1: { bsonType: "string" },
                  addressLine2: { bsonType: "string" },
                  city: { bsonType: "string" },
                  state: { bsonType: "string" },
                  zipCode: { bsonType: "string" }
                }
              }
            }
          }
        },
        personalDocuments: {
          bsonType: "array",
          description: "Array of personal documents, categorized by type",
          items: {
            bsonType: "object",
            required: ["type", "path", "title"],
            properties: {
              id: { bsonType: "string" },
              type: {
                bsonType: "string",
                enum: ["DRIVER_LICENSE_PROOF", "WORK_AUTHORIZATION_PROOF", "SSN_CARD", "PASSPORT", "OTHER"],
                description: "Type of document (e.g., DRIVER_LICENSE_PROOF, WORK_AUTHORIZATION_PROOF)"
              },
              path: {
                bsonType: "string",
                description: "Path to the stored document (e.g., S3 URL)"
              },
              title: { bsonType: "string" },
              comment: { bsonType: "string" },
              createDate: { bsonType: "date" }
            }
          }
        },
        applicationType: {
          bsonType: "string",
          description: "Application type - optional"
        }
      }
    }
  }
});

print("Employee collection with schema validation created successfully!");