# Rate Limiter Project Documentation

## 1. Project Overview

This is a Spring Boot-based rate limiting implementation designed to control and manage API request
rates across different endpoints and users.
The solution provides a flexible, annotation-driven approach to rate limiting that can be easily
integrated into existing Spring applications.

## 2. Approach to Rate Limiting

### 2.1 Core Concept

The rate limiter works by tracking the number of requests made by a specific user to a particular
API endpoint within a defined time window. When the number of requests exceeds the predefined limit,
subsequent requests are blocked.

### 2.2 Key Components

- **RateLimiterAspect**: Intercepts method calls annotated with `@RateLimit`
- **RateLimiterService**: Manages the core rate limiting logic
- **RateLimitKeyGenerator**: Generates unique keys for tracking request rates
- **RateLimiterProperties**: Configures rate limiting parameters

### 2.3 Request Tracking Mechanism

- Uses a sliding window approach
- Tracks requests per timestamp
- Removes outdated timestamps to maintain an accurate request count
- Supports both default and custom rate limit configurations

## 3. Data Storage Format

### 3.1 Primary Storage

- **Data Structure**: ConcurrentHashMap
    - Key: Composite key of user ID and API path
    - Value: RequestTracker object
- **RequestTracker**:
    - Uses a nested ConcurrentHashMap to track request counts by timestamp
    - Supports thread-safe operations

### 3.2 Reasons for Approach

- **Concurrency**: ConcurrentHashMap ensures thread-safe operations
- **Performance**: In-memory storage provides low-latency tracking
- **Flexibility**: Easy to configure and extend

### 3.3 Alternatives Considered

1. **Redis-based Solution**
    - Pros: Distributed rate limiting, persistence
    - Cons: Additional infrastructure complexity, potential network overhead
2. **Database-backed Solution**
    - Pros: Persistent tracking
    - Cons: Higher latency, increased database load

## 4. Execution steps

Follow the below steps.

### 4.1 Start the application

```shell
# create snapshot jar
./mvnw clean package

# create docker image
docker build -t rate-limiter .

# Start a new container from a Docker image
docker run --name blueoptima -it -p 8080:8080 rate-limiter

#to stop the docker container (optional)
docker stop blueoptima
```

### 4.2 APIs

**1. Developers api**

```shell
curl --location 'http://localhost:8080/api/v1/developers' \
--header 'user-id: user1'
```

**2. Organizations api**

```shell
curl --location 'http://localhost:8080/api/v1/organizations' \
--header 'user-id: user1'
```

## 5. Key Assumptions

1. User identification is done via a custom header (`user-id`)
2. Default rate limit is 100 requests per hour
3. Rate limits can be configured globally or per specific user-API combination
4. The system can handle relatively high concurrency
5. Requests from unauthenticated users are tracked as "anonymous"

## 6. Potential Input Parameters for Improvement

1. **IP-Based Tracking**
    - Add option to use IP address as fallback for user identification
2. **Dynamic Rate Limit Adjustment**
    - Ability to modify rate limits at runtime
3. **Distributed Rate Limiting Support**
    - Integration with external stores like Redis
4. **Weighted Request Tracking**
    - Different request types might consume different "rate limit credits"
5. **Granular Time Window Configuration**
    - More flexible time window definitions (e.g., rolling windows)

## 7. Suggested Improvements

### 7.1 Performance Enhancements

- Implement more efficient data pruning strategies
- Add configurable cleanup intervals for old request timestamps
- Consider using more memory-efficient data structures

### 7.2 Monitoring and Observability

- Add comprehensive logging and metrics
- Integrate with monitoring systems
- Provide detailed rate limit violation events

### 7.3 Advanced Features

- Support for distributed rate limiting across multiple service instances
- Machine learning-based adaptive rate limiting
- More sophisticated key generation strategies
- Support for different rate limits based on user roles or subscription tiers

### 7.4 Error Handling

- More granular error responses for rate limit violations
- Configurable backoff and retry mechanisms
- Support for different handling strategies (block, queue, throttle)

## 8. Conclusion

This rate limiter provides a flexible, annotation-driven solution for controlling API request rates.
Its design allows for easy integration and customization while maintaining good performance
characteristics.
