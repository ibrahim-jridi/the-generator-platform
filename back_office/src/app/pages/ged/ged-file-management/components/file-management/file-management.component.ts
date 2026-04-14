import {Component, ElementRef, Input, OnInit, Renderer2, ViewChild, ViewEncapsulation} from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import {HttpClient, HttpEvent, HttpEventType} from '@angular/common/http';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { saveAs } from 'file-saver';
import { forkJoin } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { DialogFolderComponent } from '../dialog-folder/dialog-folder.component';
import { DialogRenameComponent } from '../dialog-rename/dialog-rename.component';
import { FileElement, Level } from '../../models/file';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import {animate, style, transition, trigger} from "@angular/animations";

interface FileNode {
  id: number;
  name: string;
  isFolder: boolean;
  children?: FileNode[];
  size?: number;
  creationDate?: Date;
  extension?: string;
}


@Component({
  selector: 'app-file-management',
  templateUrl: './file-management.component.html',
  styleUrls: ['./file-management.component.scss'],
  animations: [

    trigger('simpleFade', [
      transition(':enter', [
        style({ opacity:0 }),
        animate(350)
      ])])]
})
export class FileManagementComponent implements OnInit {
  constructor(
    private translate: TranslateService,
    public dialog: MatDialog,
    private router: Router,
    private renderer: Renderer2,
    private http: HttpClient // Add HttpClient to constructor
  ) {}

  fileElements: FileElement[] = [];
  folders: any[] = [
    {

      id: 0,
      name: 'Root',
      isFolder: true,
      creationDate: new Date(),
      extension: '',
      size: 0,
      type: 'folder',
      path: 'Root/',
      children: [
        {
      id: 1,
      name: 'Folder1',
      isFolder: true,
      creationDate: new Date(),
      extension: '',
      size: 0,
      type: 'folder',
      path: 'Sample-Folder-1/',
      children: [
        {
          id: 3,
          name: 'NestedFolder1.1',
          isFolder: true,
          creationDate: new Date(),
          extension: '',
          size: 0,
          type: 'folder',
          path: 'Sample-Folder-1/Nested-Folder-1.1/',
          children: [
            {
              id: 5,
              name: 'File1.1.1',
              isFolder: false,
              creationDate: new Date(),
              extension: 'txt',
              size: 512,
              type: 'file',
              path: 'Sample-Folder-1/Nested-Folder-1.1/File-1.1.1/'
            }
          ]
        },
        {
          id: 4,
          name: 'File1.2',
          isFolder: false,
          creationDate: new Date(),
          extension: 'docx',
          size: 2048,
          type: 'file',
          path: 'Sample-Folder-1/File-1.2/'
        }
      ]
    },
    {
      id: 2,
      name: 'SampleFile1',
      isFolder: false,
      creationDate: new Date(),
      extension: 'pdf',
      size: 1024,
      type: 'file',
      path: 'Sample-File-1/'
    }
    ]}
  ];

  currentFolders: any[] = [...this.folders]; // Duplicate the folders array
  @Input() Admin: string;
  @ViewChild('myInput', { static: false }) myInputVariable: ElementRef;
  public navigtationPath = 'reports/';
  showNavigtationPath = '';
  folderisEmpty = false;
  level: Level;
  connecteduser;
  add = false;
  position = 'bottom-right';
  folderName;
  refreshElement;

  publish = false;
  fileName;
  data;
  fileContent;
  path: any;
  selectedFileBLOB: any;
  //TODO: To be changed
  foldersStack = [{ path: 'reports/', id: null }]; // Initialize with the root path
  //foldersStack = [];
  @ViewChild(MatMenuTrigger, { static: false }) contextMenu: MatMenuTrigger;
  fileStatus = { status: '', requestType: '', percent: 0 };
  filenames: string[] = [];
  private selectedElement: FileElement;
  private clickedEnter = true;
  form: FormGroup;

  selectedItem: number;
  externalUsers: any[] = []; // Static data for testing
  internalUsers: any[] = []; // Static data for testing
  selectedUserEx: number;
  selectedUserIn: number;
  isLoading: boolean;
  isTreeVisible: boolean;




  select = [
    { id: 1, name: 'GED.INTERNAL_USER' },
    { id: 2, name: 'GED.EXTERNAL_USER' }
  ];

  contextMenuPosition = { x: '0px', y: '0px' };

  currentFile: File;
  message = '';

  treeControl = new NestedTreeControl<any>(node => node.children);
  dataSource = new MatTreeNestedDataSource<any>();
  selectedNode: any;  // Add this property to track the selected node
  selectedFolderId: any | null = null;
  selectedTreeItemId: any | null = null;

  focusMyInput() {
    setTimeout(() => this.renderer.selectRootElement('#myInput').focus());
  }


  ngOnInit() {
    this.form = new FormGroup({
      myInput: new FormControl('')
    });
    this.connecteduser = 'sampleUser'; // Static data for testing
    this.display();
    this.selectedItem = null;
    this.selectedUserEx = null;
    this.selectedUserIn = null;
    this.dataSource.data = this.buildFileTree(this.currentFolders);
    console.log('Current folder', this.currentFolders);
  }

  hasChild = (_: number, node: any) => node.isFolder;

  buildFileTree(folders: any[]): any[] {
    return folders.map(folder => ({
      id: folder.id,
      name: folder.name,
      isFolder: folder.isFolder,
      children: folder.isFolder ? this.sortChildren(folder.children || []) : [],
      size: folder.size,
      creationDate: folder.creationDate,
      extension: folder.extension
    }));
  }

  sortChildren(children: any[]): any[] {
    return children.sort((a, b) => {
      if (!a.isFolder && b.isFolder) {
        return -1; // a is file, b is folder, so a comes first
      } else if (a.isFolder && !b.isFolder) {
        return 1; // a is folder, b is file, so b comes first
      }
      return 0; // both are files or both are folders, keep the order unchanged
    }).map(child => ({
      id: child.id,
      name: child.name,
      isFolder: child.isFolder,
      children: child.isFolder ? this.sortChildren(child.children || []) : [],
      size: child.size,
      creationDate: child.creationDate,
      extension: child.extension
    }));
  }

  onNodeClick(node: any) {
    this.selectedNode = node;  // Set the selected node
    this.folders = this.currentFolders;
    if (node.isFolder) {
      const clickedFolder = this.findFolderById(node.id, this.folders);
      console.log('clickedFolder:', clickedFolder);
      this.showloader();
      setTimeout(() => {
        this.hideloader();
        this.folders = clickedFolder.children ? clickedFolder.children : [];
        this.fileElements = this.currentFolders.slice();
        this.foldersStack.push({ path: this.showNavigtationPath + node.name + '/', id: node.id });
        this.showNavigtationPath += node.name + '/';
        this.folderisEmpty = this.currentFolders.length === 0;
        this.level = Level.TWO;
        this.treeControl.expand(node);
      }, 1000);
      console.log('Navigating to folder:', node.name);
    } else {
      console.log('Opening file:', node.name);
    }
  }

  findFolderById(id: number, folders: any[]): any {
    const findFolder = (folders: any[], folderId: number): any => {
      for (const folder of folders) {
        if (folder.id === folderId) {
          return folder;
        } else if (folder.children && folder.children.length) {
          const result = findFolder(folder.children, folderId);
          if (result) {
            return result;
          }
        }
      }
      return null;
    };

    return findFolder(folders, id);
  }


  // Method to handle folder selection in file explorer
  selectFolder(folder: any): void {
    this.selectedFolderId = folder.id;
    this.selectedTreeItemId = folder.id; // Assuming folder.id matches treeItem.id
  }

  // Method to handle tree item selection
  selectTreeItem(item: any): void {
    this.selectedTreeItemId = item.id;
    this.selectedFolderId = item.id; // Assuming treeItem.id matches folder.id
  }


  checkPermissions(code: string): boolean {
    return true; // Static data for testing
  }

  findFolderByUserName(name) {
    this.folders = []; // Static data for testing
    this.fileElements = []; // Static data for testing
  }

  refresh() {
    this.folders = []; // Static data for testing
  }

  onContextMenu(event: MouseEvent, element: any) {
    event.preventDefault();
    this.contextMenuPosition.x = event.clientX + 'px';
    this.contextMenuPosition.y = event.clientY + 'px';
    this.contextMenu.menu.focusFirstItem('mouse');
    this.selectedElement = element;
    this.contextMenu.openMenu();
  }

  display() {
    if (this.Admin == 'MyFileMangement') {
      this.foldersStack = [{ path: 'reports/', id: 0 }];
      this.showNavigtationPath = 'File Manager/';
      this.folders = [
        {
          id: 1,
          name: 'Sample Folder 1',
          isFolder: true,
          creationDate: new Date(),
          extension: '',
          size: 0,
          type: 'folder'
        },
        {
          id: 2,
          name: 'Sample File 1',
          isFolder: false,
          creationDate: new Date(),
          extension: 'pdf',
          size: 1024,
          type: 'file'
        }
      ]; // Static data for testing
      this.fileElements = this.folders; // Static data for testing
    } else if (this.Admin === 'MyFile') {
      this.foldersStack = [{ path: 'reports/' + this.connecteduser + '/', id: 0 }];
      this.showNavigtationPath = 'My Files/';
      this.folders = [
        {
          id: 3,
          name: 'My Folder 1',
          isFolder: true,
          creationDate: new Date(),
          extension: '',
          size: 0,
          type: 'folder'
        },
        {
          id: 4,
          name: 'My File 1',
          isFolder: false,
          creationDate: new Date(),
          extension: 'docx',
          size: 2048,
          type: 'file'
        }
      ]; // Static data for testing
      this.fileElements = this.folders; // Static data for testing
    }
  }

  hideloader() {
    this.isLoading = false;
  }

  showloader() {
    this.isLoading = true;
  }

  navigationpath() {
    const foldername = this.showNavigtationPath.split('/')[2];
    const usernamefolder = this.showNavigtationPath.split('/')[1];
    const foldernameFileManager = this.showNavigtationPath.split('/')[3];
    return (
      this.showNavigtationPath === ('My Files/request/' + foldername + '/') ||
      this.showNavigtationPath === 'My Files/request/' ||
      this.showNavigtationPath === ('File Manager/' + usernamefolder + '/request/' + foldernameFileManager + '/') ||
      this.showNavigtationPath === ('File Manager/' + usernamefolder + '/request/')
    );
  }

  back() {
    this.message = '';
    this.currentFile = null;
    this.add = false;
    this.showloader();
    if (this.foldersStack.length > 2) {
      const currentFolder: any = this.foldersStack.pop();
      this.previous(this.foldersStack[this.foldersStack.length - 1].id);
      const currentPath = this.showNavigtationPath.slice(0, -1).split('/');
      currentPath.pop();
      this.showNavigtationPath = currentPath.join('/');
      this.showNavigtationPath += '/';
    } else {
      this.display();
    }
  }

  next(element: FileElement) {
    this.message = '';
    this.currentFile = null;
    this.add = false;
    this.showloader();

    // Commented out the document service call
    // this.documentService.getFilesFoldersInFolder(element.id).subscribe(response => {
    this.hideloader();

    // Use static folders array for testing
    const response = this.getFilesFoldersInFolder(element.id);

    this.folders = response;
    if (element !== null) {
      const lastPath = this.foldersStack.length ? this.foldersStack[this.foldersStack.length - 1].path : '';
      this.foldersStack.push({ path: lastPath + element.name + '/', id: element.id });
      this.showNavigtationPath += element.name + '/';
    }
    this.fileElements = [];
    if (response == null || response.length === 0) {
      this.folderisEmpty = true;
    } else {
      response.forEach(item => {
        this.fileElements.push(item);
        this.fileElements.sort((a, b) => a.name > b.name ? 1 : -1);
      });
      this.folderisEmpty = false;
    }
    this.level = Level.TWO;
    this.refreshElement = element;

    // Find and select the node from dataSource
    const node = this.findNodeInTree(element.id, this.dataSource.data);
    if (node) {
      this.selectedNode = node;
      this.expandToNode(node);
    }

    // Highlight the selected folder in the tree
    this.selectedFolderId = element.id;
  }

  findNodeInTree(id: string, nodes: any[]): any {
    for (let node of nodes) {
      if (node.id === id) {
        return node;
      } else if (node.children && node.children.length) {
        const result = this.findNodeInTree(id, node.children);
        if (result) {
          return result;
        }
      }
    }
    return null;
  }

  expandToNode(node: any) {
    let parentNode = this.findParentNode(node.id, this.dataSource.data);
    while (parentNode) {
      this.treeControl.expand(parentNode);
      parentNode = this.findParentNode(parentNode.id, this.dataSource.data);
    }
    this.treeControl.expand(node);
  }

  findParentNode(childId: number, nodes: any[]): any {
    for (let node of nodes) {
      if (node.children && node.children.some(child => child.id === childId)) {
        return node;
      } else if (node.children && node.children.length) {
        const result = this.findParentNode(childId, node.children);
        if (result) {
          return result;
        }
      }
    }
    return null;
  }


// Helper function to get folders and files by folder id
  getFilesFoldersInFolder(folderId: string): any[] {
    const findFolderById = (folders: any[], id: string): any => {
      for (const folder of folders) {
        if (folder.id === id) {
          return folder;
        } else if (folder.children && folder.children.length) {
          const result = findFolderById(folder.children, id);
          if (result) {
            return result;
          }
        }
      }
      return null;
    };

    const folder = findFolderById(this.folders, folderId);
    return folder ? folder.children : [];
  }

  changeValue() {
    this.selectedUserEx = null;
  }

  previous(id) {
    if (id == 0) {
      this.display();
    } else {
      this.folders = []; // Static data for testing
      this.fileElements = []; // Static data for testing
      this.hideloader(); // Hide loader after fake async call
      this.folderisEmpty = false;
    }
  }

  testPublish() {
    let x = false;
    if (this.selectedElement.privilege == 'READ_AND_WRITE' && this.publish == true) {
      x = true;
    } else if (this.selectedElement.privilege == null && this.publish == true) {
      x = true;
    }
    return x;
  }

  isFolder() {
    return this.selectedElement.isFolder;
  }

  isFile() {
    return !this.selectedElement.isFolder;
  }

  isInWorkflow() {
    let test = false;
    if (this.selectedElement.inWorkflow == false) {
      test = true;
    }
    return test;
  }

  addFolder() {
    if (!this.add) {
      this.add = true;
      this.focusMyInput();
    } else if (this.add) {
      this.add = false;
    }
  }

  onEnter() {
    if (this.clickedEnter) {
      this.showloader();
      this.clickedEnter = false;
      this.add = false; // Static data for testing
      this.folderName = ''; // Static data for testing
      this.hideloader();
      this.clickedEnter = true;
      this.refresh();
    }
  }

  deleteFolder() {
    this.refresh(); // Static data for testing
  }

  rename() {
    /*
    this.sideNavService.openModal();
    const dialogRef1 = this.dialog.open(DialogRenameComponent, {
      data: this.selectedElement,
    });
    dialogRef1.afterClosed().subscribe(() => {
      this.sideNavService.closeModal();
      this.refresh();
    });
     */
  }

  download(path) {
    const iend = path.indexOf('/');
    this.fileName = path.substring(iend + 1, path.length);
    this.folderName = path.substring(0, iend);
  }

  downloadFile() {
    // Static data for testing
  }

  private resportProgress(httpEvent: HttpEvent<string[] | Blob>): void {
    switch (httpEvent.type) {
      case HttpEventType.UploadProgress:
        this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Uploading... ');
        break;
      case HttpEventType.DownloadProgress:
        this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Downloading... ');
        break;
      case HttpEventType.ResponseHeader:
        console.log('Header returned', httpEvent);
        break;
      case HttpEventType.Response:
        if (httpEvent.body instanceof Array) {
          this.fileStatus.status = 'done';
          for (const filename of httpEvent.body) {
            this.filenames.unshift(filename);
          }
        } else {
          saveAs(
            new File([httpEvent.body!], httpEvent.headers.get('Content-Disposition').split(';')[1].split('=')[1].split('.')[0].split('"')[1]!,
              { type: `${httpEvent.headers.get('Content-Type')};charset=utf-8` }
            )
          );
          console.log('headers', httpEvent.headers.get('Content-Disposition'));
          console.log('name', httpEvent.headers.get('Content-Disposition').split(';')[1].split('=')[1].split('.')[0].split('"')[1]);
        }
        this.fileStatus.status = 'done';
        break;
      default:
        console.log(httpEvent);
        break;
    }
  }

  private updateStatus(loaded: number, total: number, requestType: string): void {
    this.fileStatus.status = 'progress';
    this.fileStatus.requestType = requestType;
    this.fileStatus.percent = Math.round((100 * loaded) / total);
  }

  downloadFolder() {
    // Static data for testing
  }

  selectFile(event) {
    this.currentFile = event.target.files.item(0);
    const fileName = this.currentFile.name.split('.');
    const extension = fileName[fileName.length - 1];
    this.message = this.currentFile.name;
  }

  preUpload() {
    this.showloader();
    this.upload(); // Static data for testing
  }

  upload() {
    const filePath = this.foldersStack[this.foldersStack.length - 1].path + this.currentFile.name;
    this.refresh(); // Static data for testing
    this.message = ''; // Static data for testing
    this.currentFile = null; // Static data for testing
    this.myInputVariable.nativeElement.value = ''; // Static data for testing
    this.hideloader();
  }

  openFile() {
    // Static data for testing
  }

  openfileNew(element) {
    // Static data for testing
  }

  ngOnDestroy(): void {
    this.dialog.closeAll();
  }


  getTooltipTextFolder(name: string, creationDate: string): string {
    return `Nom: ${name}, Date de creation: ${creationDate}`;
  }

  getTooltipTextFile(name: string, creationDate: string, size: string, type: string): string {
    return `Nom: ${name}, Date de creation: ${creationDate}, Taille: ${size} Ko, Type: ${type}`;
  }

  toggleTreeVisibility() {
    this.isTreeVisible = !this.isTreeVisible;
  }
}
