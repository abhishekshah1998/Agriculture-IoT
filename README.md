# Agriculture IoT Controller

A historical Android prototype for configuring and monitoring field irrigation, fertigation, and filtration through an SMS-connected agricultural control system.

## Product brief

**Problem:** Field operators may need to control distributed agricultural equipment where reliable app-to-cloud connectivity is unavailable or expensive.

**Users:** Farm owners, field operators, and technicians responsible for irrigation and nutrient-delivery equipment.

**Concept:** Use familiar GSM/SMS infrastructure as the command and status channel between an Android application and remote field equipment.

## Capabilities explored

- GSM authentication and equipment setup;
- field-level irrigation enable/disable controls;
- fertigation timing and iteration configuration;
- filtration configuration and status;
- field-status reporting over a selected date range; and
- local handling of sent and received SMS messages.

## Key product decisions

- **SMS as a resilient transport:** the prototype prioritized reach in environments where mobile data might be unreliable.
- **Field-level configuration:** operators could act on a specific field instead of treating the installation as one undifferentiated system.
- **Status alongside commands:** an operator needs feedback that a command was delivered and reflected in system state.
- **Task-oriented screens:** irrigation, fertigation, filtration, reporting, and settings were separated around operator jobs.

## What production validation would require

- Reliable command acknowledgements, retries, and idempotency.
- Authentication stronger than trusting an originating phone number.
- Clear offline, delayed-message, and partial-failure states.
- Usability testing with operators in field conditions.
- Audit history for commands and equipment responses.
- Safety constraints preventing invalid or damaging equipment configurations.

## Status

This repository is an early Android prototype and is not actively maintained. It should not be used to control production agricultural equipment without a full security, reliability, and hardware-safety review.
