import { Component, Input, Output, EventEmitter, OnInit, SimpleChanges } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ExternalUser} from "../../../../shared/models/external-user.model";
import {distinctUntilChanged, Observable} from "rxjs";
import { TranslateService } from "@ngx-translate/core";
import { UserType } from 'src/app/shared/enums/user-type.enum';
@Component({
  selector: 'app-search-suggestion',
  templateUrl: './search-suggestion.component.html',
  styleUrls: ['./search-suggestion.component.scss']
})
export class SearchSuggestionComponent implements OnInit {

  protected subscribes = Object.values(UserType);

  @Input() searchForm: FormGroup;
  @Input() allUsersExternalByTypeUser: ExternalUser[] = [];
  @Output() suggestionSelected: EventEmitter<any> = new EventEmitter<any>();
  @Input() selectedUser: ExternalUser | null = null;

  searchTextControl: FormControl = new FormControl('');
  protected nameClientSuggestions: string[] = [];
  protected cniSuggestions: string[] = [];
  protected codeClientSuggestions: string[] = [];
  protected nameEntrepriseSuggestions: string[] = [];
  protected rccmSuggestions: string[] = [];
  protected codeEntrepriseSuggestions: string[] = [];
  protected UserType  = Object.values(UserType);

  constructor(  private formBuilder: FormBuilder,
                // private userExternalService: ExternalUserService,
                private translateService: TranslateService,
                ) {}

  public ngOnInit(): void {
    this.initSearchForm();
    this.valueChangesFormSearch();
    this.firstload()
  }

  private initSearchForm(): void {
    this.searchForm = this.formBuilder.group({
      subscribe: [UserType.PHYSICAL],
      codeClient: [null, [Validators.required,this.validateCodeClientFormat]],
      cni: [
        '',
        [Validators.required, Validators.pattern(/^\d{4}-?\d{3}-?\d{4}$/)]
      ],
      nameClient: ['', [Validators.required, this.validateNomClientFormat]],
      codeEntreprise: [null, [Validators.required,this.validateCodeEntrepriseFormat]],
      rccm: [null],
      nameEntreprise: [null],
    });
  }
  public ngOnChanges(changes: SimpleChanges): void {
    /*
    if (changes.selectedUser && changes.selectedUser.currentValue) {
          this.initSearchForm();
          this.searchForm.patchValue({
            cni: this.selectedUser.cin,
            codeClient: this.selectedUser.identifier,
            codeEntreprise: this.selectedUser.identifier,
            nameClient: this.selectedUser.firstName + " " + this.selectedUser.lastName,
            nameEntreprise : this.selectedUser.companyName,
            rccm : this.selectedUser.rccm,
            subscribe: this.selectedUser.userType === UserType.PHYSICAL ? UserType.PHYSICAL : UserType.MORALE
          });
          this.onSuggestionSelected(this.selectedUser);
          this.searchForm.disable();
    }
     */
  }
  public valueChangesFormSearch(): void {
    this.searchForm.get('nameClient').valueChanges.pipe(distinctUntilChanged()).subscribe(() => {
      this.updateNameClientFormValues();
    });

    this.searchForm.get('codeClient').valueChanges.pipe(distinctUntilChanged()).subscribe(() => {
      this.updateCodeClientFormValues();
    });

    this.searchForm.get('cni').valueChanges.pipe(distinctUntilChanged()).subscribe(() => {
      this.updateCNIFormValues();
    });

    this.searchForm.get('nameEntreprise').valueChanges.pipe(distinctUntilChanged()).subscribe(() => {
      this.updateNameEntrepriseFormValues();
    });

    this.searchForm.get('rccm').valueChanges.pipe(distinctUntilChanged()).subscribe(() => {
      this.updateRCCMFormValues();
    });

    this.searchForm.get('codeEntreprise').valueChanges.pipe(distinctUntilChanged()).subscribe(() => {
      this.updateCodeEntrepriseFormValues();
    });
  }

  public get cni() {
    return this.searchForm?.get('cni')!;
  }

  public getTranslation(value: string): string {
    return this.translateService.instant(`abonnements.${value}`);
  }

  protected onCinInput(event) {
    const input = event.target;
    let inputValue = input.value.replace(/\D/g, '').slice(0, 11);
    if (inputValue.length > 4 && inputValue.length <= 7) {
      inputValue = inputValue.slice(0, 4) + '-' + inputValue.slice(4);
    } else if (inputValue.length > 7) {
      inputValue = inputValue.slice(0, 4) + '-' + inputValue.slice(4, 7) + '-' + inputValue.slice(7);
    }
    input.value = inputValue;
    this.searchForm.patchValue({ cni: inputValue });
  }

  protected onNoSpecialCharactersInputRCCM(event) {
    const input = event.target;
    let inputValue = input.value.replace(/[^a-zA-Z0-9\s-]/g, '');
    input.value = inputValue;
    this.searchForm.patchValue({ rccm: inputValue });
  }

  protected onNoSpecialCharactersInputClient(event) {
    const input = event.target;
    let inputValue = input.value.replace(/[^a-zA-Z0-9\s-]/g, '');
    input.value = inputValue;
    this.searchForm.patchValue({ nameClient: inputValue });
  }

  protected onNoSpecialCharactersInputEntreprise(event) {
    const input = event.target;
    let inputValue = input.value.replace(/[^a-zA-Z0-9\s-]/g, '');
    input.value = inputValue;
    this.searchForm.patchValue({ nameEntreprise: inputValue });
  }

  private validateNomClientFormat(control: FormControl): { [key: string]: any } | null {
    const nameClientRegex = /^[a-zA-ZÀ-ÿ\- ]+$/;
    if (control.value && !nameClientRegex.test(control.value)) {
      return { 'invalidNameClientFormat': true };
    }
    return null;
  }

  private validateCodeClientFormat(control: FormControl): { [key: string]: any } | null {
    const codeClientRegex = /^(c)-\d+$/i;
    if (control.value && !codeClientRegex.test(control.value)) {
      return { 'invalidCodeClientFormat': true };
    }
    return null;
  }

  private validateCodeEntrepriseFormat(control: FormControl): { [key: string]: any } | null {
    const codeClientRegex = /^(s)-\d+$/i;
    if (control.value && !codeClientRegex.test(control.value)) {
       return { 'invalidCodeClientFormat': true };
    }
    return null;
  }


  public firstload(): void {
    this.loadUsersExternalByType('PHYSICAL').subscribe(data => {
      this.allUsersExternalByTypeUser = data;
    });
  }

  public onSuggestionSelected(selectedClient : any): void {
    this.suggestionSelected.emit(selectedClient);
  }

  public onSubscribeChange(event): void {
    this.clearForm();
    const subscribeValue = event.target.value;

    if(subscribeValue == UserType.PHYSICAL){
      this.addPhysiqueValidators();
      this.clearMoraleValidators();
    }else if(subscribeValue == UserType.MORALE){
      this.clearPhysiqueValidators();
      this.addMoraleValidators();
    }

    this.loadUsersExternalByType(subscribeValue).subscribe(data => {
      this.allUsersExternalByTypeUser = data;
    });
  }

  public clearForm(): void {
    this.searchForm.reset({ subscribe: this.searchForm.value.subscribe });
  }


  protected updateNameClientFormValues(): void {
    const codeClient = this.searchForm.get('codeClient');
    const cni = this.searchForm.get('cni');
    const nameClient = this.searchForm.get('nameClient').value;

    let userExternal: ExternalUser | null;

    if (nameClient) {
      const countOccurrences = this.nameClientSuggestions.filter(suggestion => suggestion === nameClient).length;

      if (countOccurrences > 1) {
      } else {
        userExternal = this.allUsersExternalByTypeUser.find(u => {
          const fullName = `${u.firstName} ${u.lastName}`.toLowerCase();
          const reversedFullName = `${u.lastName} ${u.firstName}`.toLowerCase();
          return fullName === nameClient.toLowerCase() || reversedFullName === nameClient.toLowerCase();
        });

        if (userExternal) {
          this.searchForm.patchValue({
            codeClient: userExternal.identifier || null,
            cni: userExternal.cin || null,
          });
          this.onSuggestionSelected(userExternal)
        } else {
          codeClient.setValue(null);
          cni.setValue(null);
          this.onSuggestionSelected('error')
        }
      }
    } else {
      this.nameClientSuggestions = [];
    }
    this.populateNameClientSuggestions(nameClient);
  }

  public populateNameClientSuggestions(nameClient: string): void {
    if (nameClient && this.allUsersExternalByTypeUser) {
      let filteredSuggestions = this.allUsersExternalByTypeUser
        .filter(user => {
          const fullName = `${user.firstName} ${user.lastName}`.toLowerCase();
          const reversedFullName = `${user.lastName} ${user.firstName}`.toLowerCase();
          return fullName.startsWith(nameClient.toLowerCase()) || reversedFullName.startsWith(nameClient.toLowerCase());
        })
        .map(user => `${user.firstName} ${user.lastName}`);
      filteredSuggestions = filteredSuggestions.filter(suggestion => suggestion.toLowerCase() !== nameClient.toLowerCase());
      this.nameClientSuggestions = filteredSuggestions;
    } else {
      this.nameClientSuggestions = [];
    }
  }

  protected updateNameClientFromSuggestion(suggestion: string): void {
    this.searchForm.patchValue({
      nameClient: suggestion
    });
  }

  protected updateCNIFormValues(): void {
    const codeClient = this.searchForm.get('codeClient');
    const nameClient = this.searchForm.get('nameClient');
    const cni = this.searchForm.get('cni').value;

    let userExternal: ExternalUser | null;

    if (cni) {
      userExternal = this.allUsersExternalByTypeUser.find(u => u.cin == cni);
      if (userExternal) {
        this.searchForm.patchValue({
          codeClient: userExternal.identifier || null,
          nameClient: `${userExternal.firstName} ${userExternal.lastName}` || null,
        });
        this.onSuggestionSelected(userExternal)
      } else {
        codeClient.setValue(null);
        nameClient.setValue(null);
        this.onSuggestionSelected('error')
      }
    }else {
      this.cniSuggestions = [];
    }
    this.populateCniSuggestions(cni);
  }

  public populateCniSuggestions(cni: string): void {
    if (cni && this.allUsersExternalByTypeUser) {
      let filteredSuggestions = this.allUsersExternalByTypeUser
        .map(user => user.cin)
        .filter(suggestion => suggestion.startsWith(cni));
      const selectedCni = this.searchForm.get('cni').value;
      filteredSuggestions = filteredSuggestions.filter(suggestion => suggestion !== selectedCni);
      this.cniSuggestions = filteredSuggestions;
    } else {
      this.cniSuggestions = [];
    }
  }

  protected updateCniFromSuggestion(suggestion: string): void {
    this.searchForm.patchValue({
      cni: suggestion
    });
  }

  protected updateCodeClientFormValues(): void {
    const nameClient = this.searchForm.get('nameClient');
    const cni = this.searchForm.get('cni');
    if(this.searchForm.get('codeClient').value && this.searchForm.get('codeClient').value != null){}
    const codeClient = this.searchForm.get('codeClient').value;
    const codeClientUpperCase: string = codeClient != null ? codeClient.toUpperCase() : codeClient;

    let userExternal: ExternalUser | null;

    if (codeClient) {
      userExternal = this.allUsersExternalByTypeUser.find(u => u.identifier == codeClientUpperCase);
      if (userExternal) {
        this.searchForm.patchValue({
          cni: userExternal.cin || null,
          nameClient: `${userExternal.firstName} ${userExternal.lastName}` || null,
        });
        this.onSuggestionSelected(userExternal)
      } else {
        cni.setValue(null);
        nameClient.setValue(null);
        this.onSuggestionSelected('error')
      }
    }else{
      this.codeClientSuggestions = [];
    }
    this.populateCodeClientSuggestions(codeClientUpperCase);
  }

  public populateCodeClientSuggestions(codeClient: string): void {
    if (codeClient && this.allUsersExternalByTypeUser) {
      let filteredSuggestions = this.allUsersExternalByTypeUser
        .map(user => user.identifier)
        .filter(suggestion => suggestion.startsWith(codeClient));
      const selectedCodeClient = this.searchForm.get('codeClient').value;
      filteredSuggestions = filteredSuggestions.filter(suggestion => suggestion !== selectedCodeClient);
      this.codeClientSuggestions = filteredSuggestions;
    } else {
      this.codeClientSuggestions = [];
    }
  }

  protected updateCodeClientFromSuggestion(suggestion: string): void {
    this.searchForm.patchValue({
      codeClient: suggestion
    });
  }

  protected updateNameEntrepriseFormValues(): void {
    const codeEntreprise = this.searchForm.get('codeEntreprise');
    const rccm = this.searchForm.get('rccm');
    const nameEntreprise = this.searchForm.get('nameEntreprise').value;

    let userExternal: ExternalUser | null;

    if (nameEntreprise) {
      userExternal = this.allUsersExternalByTypeUser.find(u => u.companyName == nameEntreprise);
      if (userExternal) {
        this.searchForm.patchValue({
          codeEntreprise: userExternal.identifier || null,
          rccm: userExternal.rccm || null,
        });
        this.onSuggestionSelected(userExternal)
      } else {
        codeEntreprise.setValue(null);
        rccm.setValue(null);
        this.onSuggestionSelected('error')
      }
    }else{
      this.nameEntrepriseSuggestions = [];
    }
    this.populateNameEntrepriseSuggestions(nameEntreprise);
  }

  public populateNameEntrepriseSuggestions(nameEntreprise: string): void {
    if (nameEntreprise && this.allUsersExternalByTypeUser) {
      let filteredSuggestions = this.allUsersExternalByTypeUser
        .map(user => user.companyName)
        .filter(suggestion => suggestion.toLowerCase().startsWith(nameEntreprise.toLowerCase()));
      const selectedNameEntreprise = this.searchForm.get('nameEntreprise').value;
      filteredSuggestions = filteredSuggestions.filter(suggestion => suggestion !== selectedNameEntreprise);
      this.nameEntrepriseSuggestions = filteredSuggestions;
    } else {
      this.nameEntrepriseSuggestions = [];
    }
  }

  protected updateNameEntrepriseFromSuggestion(suggestion: string): void {
    this.searchForm.patchValue({
      nameEntreprise: suggestion
    });
  }

  protected updateRCCMFormValues(): void {
    const codeEntreprise = this.searchForm.get('codeEntreprise');
    const nameEntreprise = this.searchForm.get('nameEntreprise');
    const rccm = this.searchForm.get('rccm').value;

    let userExternal: ExternalUser | null;

    if (rccm) {
      userExternal = this.allUsersExternalByTypeUser.find(u => u.rccm == rccm);
      if (userExternal) {
        this.searchForm.patchValue({
          codeEntreprise: userExternal.identifier || null,
          nameEntreprise: userExternal.companyName || null,
        });
        this.onSuggestionSelected(userExternal)
      } else {
        codeEntreprise.setValue(null);
        nameEntreprise.setValue(null);
        this.onSuggestionSelected('error')
      }
    }else{
      this.rccmSuggestions = [];
    }
    this.populateRCCMSuggestions(rccm);
  }

  public populateRCCMSuggestions(rccm: number): void {
    if (rccm && this.allUsersExternalByTypeUser) {
      let filteredSuggestions = this.allUsersExternalByTypeUser
        .map(user => user.rccm.toString())
        .filter(suggestion => suggestion.startsWith(rccm.toString()));
      const selectedRccm = this.searchForm.get('rccm').value;
      filteredSuggestions = filteredSuggestions.filter(suggestion => suggestion !== selectedRccm.toString());
      this.rccmSuggestions = filteredSuggestions;
    } else {
      this.rccmSuggestions = [];
    }
  }

  protected updateRCCMFromSuggestion(suggestion: string): void {
    this.searchForm.patchValue({
      rccm: suggestion
    });
  }

  protected updateCodeEntrepriseFormValues(): void {
    const codeEntreprise = this.searchForm.get('codeEntreprise').value;
    const rccm = this.searchForm.get('rccm');
    const nameEntreprise = this.searchForm.get('nameEntreprise');

    let userExternal: ExternalUser | null;
    const codeEntrepriseUpperCase: string = codeEntreprise != null ? codeEntreprise.toUpperCase() : codeEntreprise;

    if (codeEntreprise) {
      userExternal = this.allUsersExternalByTypeUser.find(u => u.identifier == codeEntrepriseUpperCase);
      if (userExternal) {
        this.searchForm.patchValue({
          nameEntreprise: userExternal.companyName || null,
          rccm: userExternal.rccm || null,
        });
        this.onSuggestionSelected(userExternal)
      } else {
        nameEntreprise.setValue(null);
        rccm.setValue(null);
        this.onSuggestionSelected('error')
      }
    }else{
      this.codeEntrepriseSuggestions = [];
    }
    this.populateCodeEntrepriseSuggestions(codeEntrepriseUpperCase);
  }

  public populateCodeEntrepriseSuggestions(codeEntreprise: string): void {
    if (codeEntreprise && this.allUsersExternalByTypeUser) {
      let filteredSuggestions = this.allUsersExternalByTypeUser
        .map(user => user.identifier)
        .filter(suggestion => suggestion.startsWith(codeEntreprise));
      const selectedCodeEntreprise = this.searchForm.get('codeEntreprise').value;
      filteredSuggestions = filteredSuggestions.filter(suggestion => suggestion !== selectedCodeEntreprise);
      this.codeEntrepriseSuggestions = filteredSuggestions;
    } else {
      this.codeEntrepriseSuggestions = [];
    }
  }

  protected updateCodeEntrepriseFromSuggestion(suggestion: string): void {
    this.searchForm.patchValue({
      codeEntreprise: suggestion
    });
  }

  private loadUsersExternalByType(usrType: string): Observable<any> {
    // return this.userExternalService.getAllExternalUserByType(usrType);
    return null;
  }

  public addPhysiqueValidators(): void {
    this.searchForm.get('cni')!.setValidators([Validators.required]);
    this.searchForm.get('nameClient')!.setValidators([Validators.required]);
    this.searchForm.get('codeClient')!.setValidators([Validators.required,this.validateCodeClientFormat]);
    this.searchForm.get('cni')!.updateValueAndValidity();
    this.searchForm.get('nameClient')!.updateValueAndValidity();
    this.searchForm.get('codeClient')!.updateValueAndValidity();
  }

  public clearPhysiqueValidators(): void {
    this.searchForm.get('cni')!.clearValidators();
    this.searchForm.get('nameClient')!.clearValidators();
    this.searchForm.get('codeClient')!.clearValidators();
    this.searchForm.get('cni')!.updateValueAndValidity();
    this.searchForm.get('nameClient')!.updateValueAndValidity();
    this.searchForm.get('codeClient')!.updateValueAndValidity();
  }

  public addMoraleValidators(): void {
    this.searchForm.get('nameEntreprise')!.setValidators([Validators.required,this.validateNomClientFormat]);
    this.searchForm.get('rccm')!.setValidators([Validators.required, Validators.pattern(/^[a-zA-Z0-9\s-]*$/)]);
    this.searchForm.get('codeEntreprise')!.setValidators([Validators.required,this.validateCodeEntrepriseFormat]);
    this.searchForm.get('nameEntreprise')!.updateValueAndValidity();
    this.searchForm.get('rccm')!.updateValueAndValidity();
    this.searchForm.get('codeEntreprise')!.updateValueAndValidity();
  }

  public clearMoraleValidators(): void {
    this.searchForm.get('nameEntreprise')!.clearValidators();
    this.searchForm.get('rccm')!.clearValidators();
    this.searchForm.get('codeEntreprise')!.clearValidators();
    this.searchForm.get('nameEntreprise')!.updateValueAndValidity();
    this.searchForm.get('rccm')!.updateValueAndValidity();
    this.searchForm.get('codeEntreprise')!.updateValueAndValidity();
  }

}
