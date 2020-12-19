[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) ![Badge](https://github.com/eBayClassifiedsGroup/reference-move-webhook-receiver/workflows/Java%20CI/badge.svg)
[Github Home](https://github.com/tastybug/reference-move-webhook-receiver/)

# Reference MoVe Webhook Receiver Implementation

You are the developer of an organisation that plans to receive data from eCG Motors Vertical, "MoVe". You are tasked with setting up a service that will receive event payloads like listings, promotions, dealer data and so on.
What you need to know is that this kind of integration is done using a webhook contract. MoVe defines certain payload structures and expects a recipient to provide certain remote endpoints via which these payloads can be handed over. More on the event types and payloads down below.

This repository contains a reference webhook receiver implementation in plain Java using Spring Boot to pull in a number of required dependencies. You can use this as a baseline for your own internal development or just to see how certain things can be handled.

## Setting up shop as a developer
### Building

This project utilizes gradle, you can build it using `./gradlew clean build`. During build time, a number of payload types are generated for you based on the public MoVe API spec, e.g. `Listing.java`. Each of these payload types comes with helpful descriptions and, where applicable, with value ranges that you can expect.

In your IDE make sure to enable annotation processing.

### Running locally

The service is stateless and can be started locally by running `./gradlew clean bootRun`, after which you can open <http://localhost:8080/swagger-ui.html>.

This will open Swagger UI, which is a simple REST client that you can use to publish events into the service and see how it reacts.

At the bottom of the page your can browse through the MoVe API model, complete with examples, validation rules and description of semantics. Make sure to check those out to map incoming data to your internal model.

![Swagger UI as a source to inspect MoVe's domain model](./use-swagger-to-inspect-domain-model.png)