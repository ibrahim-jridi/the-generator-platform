import { AfterViewInit, Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog-rename',
  templateUrl: './dialog-rename.component.html',
  styleUrls: ['./dialog-rename.component.scss']
})
export class DialogRenameComponent implements OnInit, AfterViewInit {
  oldName: any;
  newName: string;
  isFolder: boolean;
  formGroup!: FormGroup;

  constructor(
    public dialogRefRenameComponent: MatDialogRef<DialogRenameComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder
  ) {
    this.oldName = data.name;
    this.newName = data.name;
    this.isFolder = data.isFolder;
  }

  ngOnInit(): void {
    this.formGroup = this.createForm();
  }

  ngAfterViewInit(): void {
    this.formGroup = this.createForm();
  }

  closeDialog(value: boolean): void {
    this.dialogRefRenameComponent.close(value);
  }

  createForm(): FormGroup {
    if (this.isFolder) {
      return this.formBuilder.group({
        name: [
          this.newName,
          {
            validators: [Validators.required],
            //asyncValidators: this.fileFolderNameValidator.folderNameValidator(this.data.id, this.data.idParent),
            updateOn: 'change'
          }
        ]
      });
    } else {
      return this.formBuilder.group({
        name: [
          this.newName,
          {
            validators: [Validators.required],
            //asyncValidators: this.fileFolderNameValidator.fileNameValidator(this.data.path),
            updateOn: 'blur'
          }
        ]
      });
    }
  }

  onSubmit(): void {
    // TODO update submit method
  }
}
