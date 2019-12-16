package de.unipassau.sep19.hafenkran.reportingservice.controller;

import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTO;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result;
import de.unipassau.sep19.hafenkran.reportingservice.service.ResultService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * The REST-Controller for result specific POST- and GET-Endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/results")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResultController {

    private final ResultService resultService;

    /**
     * GET-Endpoint for receiving the csv- and log-results of an execution with the {@code executionId} as a JSON.
     *
     * @param executionId The execution to get the results from.
     * @return An {@link de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList} of all results from the
     *         execution for making charts available.
     */
    @GetMapping("/{executionId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ResultDTO> getResultsForExecutionId(@NonNull @PathVariable UUID executionId) {
        return resultService.retrieveResultDTOListByExecutionId(executionId);
    }

    /**
     * GET-Endpoint for receiving all results of the execution with the {@code executionId} as a String.
     *
     * @param executionId The id from the execution to get the results from.
     * @param refreshString A boolean represented as string, which toggles the refresh of the resources.
     * @return A Base64-String from the results for making a download available.
     */
    @GetMapping(value = "/{executionId}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public byte[] downloadResultsForExecution(@NonNull @PathVariable UUID executionId, @RequestParam("refresh") String refreshString) {
        return resultService.downloadResultsAsBase64(executionId, refreshString.equals("true"));
    }
}
