import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, lastValueFrom,map, Observable, throwError } from 'rxjs';
import { RequestType } from '../enums/requestType';
import { GlobalService } from './global.service';
import { ProcessInstance } from '../models/processInstance.model';
import { Task } from '../models/task.model';
import { HttpErrorResponse, HttpParams, HttpResponse } from '@angular/common/http';
import { HistoricTaskInstance } from '../models/historicTaskInstance.model';
import { AppToastNotificationService } from './appToastNotification.service';
import { TokenUtilsService } from './token-utils.service';
import { DmnActivityDecisionDefinition } from '../models/dmn-activity-decision-definition.model';
import { PaginationArgs } from '../models/paginationArgs.model';
import { Deploy } from '../models/deploy.model';
import { ProcessDefinitionCriteria } from '../models/process-definition-criteria.model';
import { ProcessInstanceCriteria } from '../models/process-instance-criteria';
import { DecisionDefinitionCriteria } from '../models/decision-definition-criteria';
import { TaskCriteria } from '../models/task-criteria';
import { ProcessDefinition } from '../models/processDefinition.model';
import { PaginatedResponse } from '../models/paginated-response.model';
import { ProcessInstanceHistoryCriteria } from '../models/process-instance-history-criteria';
import { ProcessInstanceHistory } from '../models/process-instance-history';

@Injectable({
  providedIn: 'root'
})
export class CamundaService {
  userId: string;
  private readonly BASE_WORKFLOW_URL = this.globalService.BASE_WORKFLOW_URL + this.globalService.API_V1_URL + 'camunda';
  taskValidateSubject = new BehaviorSubject<number>(null);
  taskValidateObservable = this.taskValidateSubject.asObservable();

  constructor(
    private readonly globalService: GlobalService,
    private readonly toasterService: AppToastNotificationService,
    private readonly tokenUtilsService: TokenUtilsService
  ) {}

  // --- Process Management ---
  public getDeployedProcesses(): Observable<any> {
    const url = `${this.BASE_WORKFLOW_URL}/list-process`;
    return this.globalService.call(RequestType.GET, url);
  }

  public getDeployedProcessesAsPromise() : Promise<any>{
    return lastValueFrom(this.getDeployedProcesses());
  }

  public getAllDecisionDefinition(): Observable<DmnActivityDecisionDefinition[]> {
    const url = `${this.BASE_WORKFLOW_URL}/fetch-all-decision-definition`;
    return this.globalService.call(RequestType.GET, url);
  }

  public getAllDecisionDefinitionAsPromise(): Promise<DmnActivityDecisionDefinition[]> {
    return this.getAllDecisionDefinition().toPromise();
  }

  public startProcess(processKey: string): Observable<ProcessInstance> {
    this.userId = this.tokenUtilsService.getUserId();
    let variables = {
      starter: this.userId
    };
    const url = `${this.BASE_WORKFLOW_URL}/start/${processKey}`;
    return this.globalService.call(RequestType.POST, url, variables);
  }
  public startProcessbyVariable(processKey: string,variables:object): Observable<ProcessInstance> {
   
    const url = `${this.BASE_WORKFLOW_URL}/start/${processKey}`;
    return this.globalService.call(RequestType.POST, url, variables);
  }
  public startProcessOnceByKey(processKey: string, vars: object): Observable<ProcessInstance> {
    const url = `${this.BASE_WORKFLOW_URL}/start-process-once/${processKey}`;
    return this.globalService.call(RequestType.POST, url, vars);
  }

  public startProcessOnceByKeyAndGetStartedInstance(processKey: string, vars: object): Observable<ProcessInstance> {
    const url = `${this.BASE_WORKFLOW_URL}/start-process-once-and-get-started-instance/${processKey}`;
    return this.globalService.call(RequestType.POST, url, vars);
  }

  public startProcessById(processId: string): Observable<ProcessInstance> {
    this.userId = this.tokenUtilsService.getUserId();
    let variables = {
      starter: this.userId
    };
    const url = `${this.BASE_WORKFLOW_URL}/startById/${processId}`;
    return this.globalService.call(RequestType.POST, url, variables);
  }

  public getProcessXmlById(processId: string): Observable<string> {
    const url = `${this.BASE_WORKFLOW_URL}/xml/${processId}`;
    return this.globalService.call(RequestType.GET, url, { responseType: 'text' });
  }

  public getDecisionDefinitionById(processDmnId: string): Observable<string> {
    const url = `${this.BASE_WORKFLOW_URL}/fetch-decision-definition-by-id/${processDmnId}`;
    return this.globalService.call(RequestType.GET, url, { responseType: 'text' });
  }

  public toggleProcessDefinitionState(processDefinitionId: string): Observable<boolean> {
    const url = `${this.BASE_WORKFLOW_URL}/toggle-state/${processDefinitionId}`;
    return this.globalService.call(RequestType.POST, url, {});
  }

  // --- Repository Management ---
  public deployProcessDefinition(processData: any): Observable<Deploy> {
    const url = `${this.BASE_WORKFLOW_URL}/repository/deploy`;
    return this.globalService.call(RequestType.POST, url, processData);
  }

  public deployDmnDefinition(dmnData: any): Observable<Deploy> {
    const url = `${this.BASE_WORKFLOW_URL}/repository/deploy-dmn`;
    return this.globalService.call(RequestType.POST, url, dmnData);
  }

  public getProcessInstance(processDefinitionId: string): Observable<ProcessInstance[]> {
    const url = `${this.BASE_WORKFLOW_URL}/process-instances/${processDefinitionId}`;
    return this.globalService.call(RequestType.GET, url).pipe(map((data: any[]) => data.map((item) => new ProcessInstance(item))));
  }

  public getTasksByAssignee(userId: string): Observable<Task[]> {
    return this.globalService
      .call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/tasks-by-user/${userId}`)
      .pipe(catchError(this.handleError.bind(this)));
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      this.toasterService.onError('An error occurred:', error.error.message);
    } else {
      this.toasterService.onError(`Backend returned code ${error.status}`, `Error: ${error.error}`);
    }
    return throwError(() => new Error('Something went wrong; please try again later.'));
  }

  public getHistoricTasks(processInstanceId: string): Observable<HistoricTaskInstance[]> {
    return this.globalService.call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/history/tasks/${processInstanceId}`).pipe(
      // map((data: any[]) => data.map(item => new HistoricTaskInstance())), // Map the data to HistoricTaskInstance model
      catchError((error) => {
        this.toasterService.onError('Failed to load tasks', 'Error');
        return throwError(() => error);
      })
    );
  }

  public getTaskByProcessInstanceId(processInstanceId: string): Observable<Task> {
    return this.globalService.call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/current-task/${processInstanceId}`);
  }

  public getTaskById(taskId: string): Observable<Task> {
    return this.globalService.call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/current-task/by-id/${taskId}`);
  }

  public validateTask(taskId: string, variables: any): Observable<Task> {
    return this.globalService.call(RequestType.POST, `${this.BASE_WORKFLOW_URL}/validate-task/${taskId}`, variables);
  }

  public getCurrentUserProcessInstancesByKey(processKey: string): Observable<ProcessInstance[]> {
    const url = `${this.BASE_WORKFLOW_URL}/process-instances-currentUser-byKey/${processKey}`;
    return this.globalService.call(RequestType.GET, url).pipe(
      map((data: any[]) => data.map((item) => new ProcessInstance(item))),
      catchError(this.handleError)
    );
  }

  public getTasksByAssigneeGroupId(userId: string): Observable<Task[]> {
    return this.globalService
      .call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/tasks-by-group/${userId}`)
      .pipe(catchError(this.handleError.bind(this)));
  }

  public getTaskByProcessInstanceIdAndUserId(processInstanceId: string, userId: string): Observable<Task> {
    return this.globalService.call(RequestType.GET, this.BASE_WORKFLOW_URL + '/current-task-by-userId/' + processInstanceId + '/' + userId);
  }

  public getHistoricTasksByAssignee(userId: string, pageable: PaginationArgs): Observable<HttpResponse<Task[]>> {
    let params = new HttpParams().set('page', pageable.page).set('size', pageable.size).set('sort', pageable.sort);

    return this.globalService
      .call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/historic-tasks-by-user/${userId}`, {
        params: params,
        observe: 'response'
      })
      .pipe(catchError(this.handleError.bind(this)));
  }

  public getActiveTasksByAssignee(pageable: PaginationArgs): Observable<HttpResponse<Task[]>> {
    let params = new HttpParams().set('page', pageable.page).set('size', pageable.size).set('sort', pageable.sort);

    return this.globalService
      .call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/active-tasks-by-user`, { params: params, observe: 'response' })
      .pipe(catchError(this.handleError.bind(this)));
  }

  public getProcessVariablesByProcessInstanceId(processInstanceId: string): Observable<any> {
    let params = new HttpParams().set('processInstanceId', processInstanceId);

    return this.globalService
      .call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/process/variables`, { params: params, observe: 'response' })
      .pipe(catchError(this.handleError.bind(this)));
  }

  public getDeployedProcessesByCriteria(criteria: ProcessDefinitionCriteria, paginationArgs: PaginationArgs): Observable<any> {
    const url = `${this.BASE_WORKFLOW_URL}/list-process`;
    return this.globalService.call(RequestType.POST, url, criteria, { params: paginationArgs });
  }

  public getProcessInstanceByCriteria(
    criteria: ProcessInstanceCriteria,
    processDefinitionId: string,
    paginationArgs: PaginationArgs
  ): Observable<any> {
    const url = `${this.BASE_WORKFLOW_URL}/process-instances/${processDefinitionId}`;
    return this.globalService.call(RequestType.POST, url, criteria, { params: paginationArgs });
  }

  public getAllDecisionDefinitionByCriteria(
    criteria: DecisionDefinitionCriteria,
    paginationArgs: PaginationArgs
  ): Observable<PaginatedResponse<DmnActivityDecisionDefinition>> {
    const url = `${this.BASE_WORKFLOW_URL}/fetch-all-decision-definition`;
    return this.globalService.call(RequestType.POST, url, criteria, { params: paginationArgs });
  }

  public getHistoricTasksByAssigneeAndCriteria(
    userId: string,
    criteria: TaskCriteria,
    pageable: PaginationArgs
  ): Observable<PaginatedResponse<Task>> {
    let params = new HttpParams().set('page', pageable.page).set('size', pageable.size).set('sort', pageable.sort);
    return this.globalService.call(RequestType.POST, `${this.BASE_WORKFLOW_URL}/historic-tasks-by-user/${userId}`, criteria, {
      params: params
    });
  }

  public getProcessDefinitionById(id: string): Observable<ProcessDefinition> {
    return this.globalService.call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/process-definition/${id}`);
  }

  public getActiveTasksByAssigneeAndCriteria(criteria: TaskCriteria, pageable: PaginationArgs): Observable<PaginatedResponse<Task>> {
    return this.globalService.call(RequestType.POST, `${this.BASE_WORKFLOW_URL}/active-tasks-by-user`, criteria, { params: pageable });
  }

  public getActiveTasksByUserId( pageable: PaginationArgs): Observable<PaginatedResponse<Task>> {
    return this.globalService.call(RequestType.GET, `${this.BASE_WORKFLOW_URL}/active-tasks-by-user`, { params: pageable });
  }
  public getProcessByCategory(category: string): Observable<any> {
    const url = `${this.BASE_WORKFLOW_URL}/process-by-category?category=${category}`;
    return this.globalService.call(RequestType.GET, url);
  }
  public startProcessByVariable(processKey: string,variables:object): Observable<ProcessInstance> {
    const url = `${this.BASE_WORKFLOW_URL}/start/${processKey}`;
    return this.globalService.call(RequestType.POST, url, variables);
  }
  public startUniqueProcessInstanceByKey(processKey: string, vars: object): Observable<ProcessInstance> {
    const url = `${this.BASE_WORKFLOW_URL}/start-unique-process/${processKey}`;
    return this.globalService.call(RequestType.POST, url, vars);
  }
  public getHistoricProcessInstanceByUserId(
    userId: string,
    criteria: ProcessInstanceHistoryCriteria,
    pageable: PaginationArgs
  ): Observable<PaginatedResponse<ProcessInstanceHistory>> {
    let params = new HttpParams().set('page', pageable.page).set('size', pageable.size).set('sort', pageable.sort);
    return this.globalService.call(RequestType.POST, `${this.BASE_WORKFLOW_URL}/process-instances-userId/${userId}`, criteria, {
      params: params
    });
  }
  public startProcessByVariables(processInstanceId: string, processDefinitionKey: string): Observable<any> {
    this.userId = this.tokenUtilsService.getUserId();

    const body = {
       processInstanceId: processInstanceId,
      processDefinitionKey: processDefinitionKey,
      variables: {
        starter: this.userId
      }
    };

    const url = `${this.BASE_WORKFLOW_URL}/start`;
    return this.globalService.call(RequestType.POST, url, body);
  }

  public getProcessInstancesByUserId(userId: string, processDefinitionKey: string): Observable<HistoricTaskInstance> {
    const url = `${this.BASE_WORKFLOW_URL}/process-instance/${userId}?processDefinitionKey=${processDefinitionKey}`;
    return this.globalService.call(RequestType.GET, url);
  }

}
