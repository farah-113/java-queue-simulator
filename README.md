# Multithreaded Queue Management Simulator

A Java Swing desktop application designed to simulate customer arrivals, queue scheduling, and concurrent service processing.

## Project Overview

This project explores multithreading, concurrent programming, and scheduling algorithms through a configurable queue management simulation.

Customers are generated with randomized arrival and service times and assigned to service queues according to a selected scheduling strategy.

The graphical interface visualizes customers, queue states, and simulation events.

## Features

- Configurable number of customers and service queues
- Configurable simulation duration
- Randomized arrival and service times
- Input validation
- Selectable scheduling strategies
- Graphical visualization of queue states
- Simulation event logging
- Average waiting time calculation
- Average service time calculation
- Peak hour identification

## Scheduling Strategies

The Strategy design pattern separates scheduling algorithms from the main simulation logic.

| Strategy | Description |
|----------|-------------|
| Shortest Queue | Selects the queue with the fewest tasks |
| Shortest Waiting Time | Selects the queue with the lowest accumulated remaining service time |

## Concurrency Architecture

The application is designed around independent service workers and a separate simulation management thread.

Concurrency mechanisms include:

- Runnable implementations for service workers and simulation management
- BlockingQueue and LinkedBlockingQueue for thread-safe task storage
- AtomicInteger for tracking queue workload
- Volatile variables for shared execution state
- Synchronized access to shared resources
- SwingUtilities.invokeLater for graphical updates on the Event Dispatch Thread

## Software Architecture

| Package | Responsibility |
|---------|----------------|
| application | Graphical interface and visualization |
| logic | Scheduling, simulation management, task generation, and logging |
| model | Customer tasks, service queues, and simulation configuration |

## Technical Implementation

- Object-oriented programming
- Strategy design pattern
- Multithreading and concurrent programming
- Thread-safe data structures
- Atomic operations
- Scheduling algorithms
- Java Collections Framework
- Java Streams
- Event-driven graphical interface
- File-based simulation logging

## Simulation Statistics

The simulation calculates:

- Average waiting time
- Average service time
- Peak hour

Simulation snapshots and final statistics are recorded in a local testsim.txt file.

## Technologies

- Java 25
- Java Swing
- Java Concurrency API
- Maven
- IntelliJ IDEA
- UML

## Repository Structure

- `src/main/java/application/` - Graphical user interface
- `src/main/java/logic/` - Scheduling and simulation logic
- `src/main/java/model/` - Domain models
- `src/logs/` - Sample simulation logs
- `uml/` - Software architecture diagrams
- `pom.xml` - Maven configuration

## Running the Application

1. Clone or download the repository.
2. Open the project in IntelliJ IDEA.
3. Import the project using its Maven configuration.
4. Configure JDK 25 as the project SDK.
5. Run the application.SimulationFrame main class.
6. Configure the simulation parameters.
7. Select the desired scheduling strategy.
8. Validate the input and start the simulation.

## Documentation

- [Class Diagram](uml/class.drawio)
- [Package Diagram](uml/package.drawio)
- [Use Case Diagram](uml/usecase.drawio)

The UML diagrams can be opened using diagrams.net.

## Sample Simulation Logs

- [Simulation Example 1](src/logs/test1.txt)
- [Simulation Example 2](src/logs/test2.txt)

## Current Limitations

The archived implementation contains the service worker logic, but worker thread initialization is currently disabled in the scheduler.

Worker execution must be restored before reproducing complete queue-processing behavior.

## Future Improvements

- ExecutorService-based thread pool management
- Improved thread lifecycle management
- Automated concurrency and scheduling tests
- Deterministic simulation using configurable random seeds
- Comparative performance analysis of scheduling strategies
- Improved synchronization and statistical accuracy
