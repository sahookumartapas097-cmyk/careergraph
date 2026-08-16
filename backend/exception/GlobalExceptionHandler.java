package com.wexa.careergraph.exception;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(Exception.class)
 public ResponseEntity<Map<String,Object>> handle(Exception ex){
  return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
   "timestamp",Instant.now().toString(),"status",503,"error","Service Unavailable",
   "message","CareerGraph could not complete the request. Check CognoDB connection."));
 }
}
