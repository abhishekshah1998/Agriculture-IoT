# Agriculture IoT Controller

An Android prototype for configuring and monitoring SMS-connected agricultural field operations, including filtration and fertigation workflows.

> **Project status:** archival prototype. It demonstrates the mobile control concept and is not production-ready for operating agricultural equipment.

## Product concept

Remote field equipment may operate where data connectivity is limited but cellular SMS is available. This prototype explores a lightweight control loop between an Android phone and a GSM-enabled field controller:

```text
Operator input → Android validation → SMS command → Field controller
                                                        │
Status screen ← Parsed SMS response ← Cellular network ─┘
```

## Prototype capabilities

- Selects a controller contact from the phone and stores it locally with SQLite.
- Sends SMS-based configuration and control commands.
- Supports field setup across multiple field numbers.
- Configures filtration timing parameters.
- Enables or disables fertigation with delay, run-time, and iteration inputs.
- Reads recent SMS responses from the configured controller number for status display.
- Includes GSM authentication and controller settings flows.

## Technology

- Java
- AndroidX
- Android SDK 30; minimum SDK 16
- Native Android SMS, contacts, and SQLite APIs
- Gradle Android plugin 4.0.2

## Explore the code

- [`GsmAuthenticationActivity.java`](app/src/main/java/com/example/myapplication/GsmAuthenticationActivity.java) selects and authenticates the controller contact.
- [`FieldConfiguration.java`](app/src/main/java/com/example/myapplication/FieldConfiguration.java) constructs field configuration commands.
- [`FieldFiltration.java`](app/src/main/java/com/example/myapplication/FieldFiltration.java) handles filtration parameters and SMS commands.
- [`FieldFertigation.java`](app/src/main/java/com/example/myapplication/FieldFertigation.java) handles fertigation controls.
- [`DatabaseHandler.java`](app/src/main/java/com/example/myapplication/DatabaseHandler.java) stores the selected controller contact locally.

## Build notes

The repository preserves the original prototype. Building it today may require an older Android Studio/JDK environment or migration away from JCenter and the historical Gradle configuration.

The application requests sensitive SMS and contacts permissions. Use a test device, a test SIM, and a non-production controller endpoint. Review all generated commands before connecting the application to physical equipment.

## What this project demonstrates

The prototype explores resilient interaction design for constrained connectivity, translating operational parameters into device commands, and closing the loop with status feedback. A production product would require an authenticated command protocol, encryption, command acknowledgements, audit logs, offline-state handling, hardware fail-safes, and usability testing with field operators.
