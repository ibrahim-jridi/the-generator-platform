import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog-folder',
  templateUrl: './dialog-folder.component.html',
  styleUrls: ['./dialog-folder.component.scss']
})
export class DialogFolderComponent implements OnInit {

  savedForLater: any;

  constructor(
    public dialogRef: MatDialogRef<DialogFolderComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.savedForLater = data;
  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
