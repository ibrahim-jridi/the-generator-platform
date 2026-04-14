import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Connector} from "../../../shared/models/connector.model";
import {AuthenticationMethod, authenticationMethods} from "../../../shared/enums/authentication-method.enum";
import {RequestMethod, requestMethods} from "../../../shared/enums/request-method.enum";
import {ContentType, contentTypes} from "../../../shared/enums/content-type.enum";
import {RegexConstants} from "../../../shared/utils/regex-constants";
import {TranslatePipe} from "@ngx-translate/core";
import {ConnectorService} from "../../../shared/services/connector.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AppToastNotificationService} from "../../../shared/services/appToastNotification.service";

@Component({
  selector: 'app-add-connector',
  templateUrl: './add-connector.component.html',
  styleUrl: './add-connector.component.scss'
})
export class AddConnectorComponent implements OnInit{

  public connectorForm: FormGroup;
  public newConnector = new Connector();
  public authenticationMethods = Object.values(authenticationMethods);
  public requestMethods = Object.values(requestMethods);
  public contentTypes = Object.values(contentTypes);
  public messageLength: number = 0;
  public isTokenFieldVisible = false;
  public tokenFieldLabel = '';
  public tokenPlaceholder = '';

  constructor(
    private fb: FormBuilder,private translatePipe: TranslatePipe, private connectorService: ConnectorService,
    private router: Router, private route: ActivatedRoute, private toastrService: AppToastNotificationService
  ) { }

  ngOnInit(): void {
    this.handleOnInitConnectorForm();
  }

  public handleOnInitConnectorForm() {
    this.connectorForm = this.fb.group({
      url: [
        '',
        [Validators.required]
      ],
      apiName: [
        '',
        [Validators.required]
      ],
      description: [
        '',
        [Validators.required]
      ],
      requestMethod: [
        null,
        [Validators.required]
      ],
      authenticationMethod: [
        null,
        [Validators.required]
      ],
      contentType: [
        null,
        [Validators.required]
      ],
      connectTimeout: [
        '',
        [Validators.pattern(RegexConstants.NUMERIC), Validators.min(0)]
      ],
      readTimeout: [
        '',
        [Validators.pattern(RegexConstants.NUMERIC), Validators.min(0)]
      ],
      retryCount: [
        '',
        [Validators.pattern(RegexConstants.NUMERIC), Validators.min(0)]
      ],
      retryInterval: [
        '',
        [Validators.pattern(RegexConstants.NUMERIC), Validators.min(0)]
      ],
      token: [
        '',
        [Validators.required]
      ],
      requestBody: [
        '',
        [Validators.required]
      ],
      responseBody: [
        '',
        [Validators.required]
      ],
    })
  }

  public get description() {
    return this.connectorForm?.get('description')!;
  }

  public updateCharacterCount(): void {
    this.messageLength = this.description.value?.length;
  }

  onAuthMethodChange(selectedMethod): void {
    this.isTokenFieldVisible = !!selectedMethod;

    // Update label and placeholder based on the selected authentication method
    switch (selectedMethod.name) {
      case AuthenticationMethod.BASIC_AUTH:
        this.tokenFieldLabel = this.translatePipe.transform('connector.coltoken_basic_auth');
        this.tokenPlaceholder = this.translatePipe.transform('connector.add.inserttoken_basic_auth');
        break;
      case AuthenticationMethod.API_KEY_AUTH:
        this.tokenFieldLabel = this.translatePipe.transform('connector.coltoken_api_key_auth');
        this.tokenPlaceholder = this.translatePipe.transform('connector.add.inserttoken_api_key_auth');
        break;
      case AuthenticationMethod.TOKEN_BASED_AUTH:
        this.tokenFieldLabel = this.translatePipe.transform('connector.coltoken_token_based_auth');
        this.tokenPlaceholder = this.translatePipe.transform('connector.add.inserttoken_token_based_auth');
        break;
      default:
        this.tokenFieldLabel = '';
        this.tokenPlaceholder = '';
        break;
    }

    // Clear the token field when the authentication method changes
    this.connectorForm.get('token')?.setValue('');
  }

  public handleOnAddConnector() {
    this.newConnector.url = this.connectorForm.get('url')?.value;
    this.newConnector.apiName = this.connectorForm.get('apiName')?.value;
    this.newConnector.description = this.connectorForm.get('description')?.value;
    this.newConnector.authenticationMethod = this.connectorForm.get('authenticationMethod')?.value.name;
    this.newConnector.contentType = this.connectorForm.get('contentType')?.value.name;
    this.newConnector.requestMethod = this.connectorForm.get('requestMethod')?.value.name;
    this.newConnector.connectTimeout = this.connectorForm.get('connectTimeout')?.value;
    this.newConnector.readTimeout = this.connectorForm.get('readTimeout')?.value;
    this.newConnector.retryCount = this.connectorForm.get('retryCount')?.value;
    this.newConnector.retryInterval = this.connectorForm.get('retryInterval')?.value;
    this.newConnector.token = this.connectorForm.get('token')?.value;
    this.newConnector.requestBody = this.connectorForm.get('requestBody')?.value;
    this.newConnector.responseBody = this.connectorForm.get('responseBody')?.value;

    this.connectorService.saveConnector(this.newConnector).subscribe({
      next: (response) => {
        this.navigateToConnectors();
        this.toastrService.onSuccess(this.translatePipe.transform(''),  this.translatePipe.transform('menu.SUCCESS'));
      },
      error: error => {
        this.toastrService.onError(this.translatePipe.transform(''),  this.translatePipe.transform('menu.ERROR'));
      }
    })
  }

  public navigateToConnectors(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

}
