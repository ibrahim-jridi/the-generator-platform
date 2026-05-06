// Complete rewritten component with proper recursive tree building

import { Component, ElementRef, Input, OnInit, Renderer2, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { saveAs } from 'file-saver';
import { TranslateService } from '@ngx-translate/core';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { animate, style, transition, trigger } from '@angular/animations';
import { FileManagementService, FolderDto, FileDto } from '../../../../../shared/services/file.service';

interface TreeNode {
  id: string;
  name: string;
  isFolder: boolean;
  children?: TreeNode[];
  level?: number;
  path?: string;
  folderMinioId?: string;
  size?: string;
  extension?: string;
  type?: string;
  creationDate?: Date;
}

@Component({
  selector: 'app-file-management',
  templateUrl: './file-management.component.html',
  styleUrls: ['./file-management.component.scss'],
  animations: [trigger('simpleFade', [transition(':enter', [style({ opacity: 0 }), animate(350)])])]
})
export class FileManagementComponent implements OnInit {
  @Input() Admin: string = '';
  @ViewChild('myInput', { static: false }) myInputVariable: ElementRef | undefined;
  @ViewChild(MatMenuTrigger, { static: false }) contextMenu: MatMenuTrigger | undefined;

  // Data properties
  currentItems: (FolderDto | FileDto)[] = []; // Combined folders and files for current view
  currentFolderPath: string = '';
  folderStack: { id: string | null; name: string; path: string }[] = [];

  // UI State
  isLoading: boolean = false;
  isTreeVisible: boolean = true;
  add: boolean = false;
  folderName: string = '';
  currentFile: File | null = null;
  message: string = '';
  selectedElement: any = null;

  // Tree control
  treeControl = new NestedTreeControl<TreeNode>((node) => node.children);
  dataSource = new MatTreeNestedDataSource<TreeNode>();
  selectedNode: TreeNode | null = null;

  // Forms
  form: FormGroup;
  contextMenuPosition = { x: '0px', y: '0px' };

  // User selection
  select = [
    { id: 1, name: 'GED.INTERNAL_USER' },
    { id: 2, name: 'GED.EXTERNAL_USER' }
  ];
  selectedItem: number | null = null;
  selectedUserEx: string | null = null;
  externalUsers: any[] = [];
  internalUsers: any[] = [];

  constructor(
    private translate: TranslateService,
    public dialog: MatDialog,
    private renderer: Renderer2,
    private fileService: FileManagementService
  ) {}

  ngOnInit() {
    this.form = new FormGroup({
      myInput: new FormControl('')
    });
    this.initializeFileManager();
  }

  initializeFileManager() {
    this.showLoader();
    this.folderStack = [{ id: null, name: 'Root', path: '' }];
    this.currentFolderPath = '';

    if (this.Admin === 'MyFile') {
      // Get current user's personal folder
      this.fileService.getUserFolder().subscribe({
        next: (userFolder) => {
          this.currentFolderPath = userFolder.path;
          this.folderStack = [{ id: userFolder.id, name: userFolder.name, path: userFolder.path }];
          this.loadFolderContentsByPath(userFolder.path);
          this.loadFolderTree();
          this.hideLoader();
        },
        error: (error) => {
          console.error('Error loading user folder:', error);
          this.message = 'Error loading your files';
          this.hideLoader();
        }
      });
    } else {
      // Admin view - load all root folders/buckets
      this.loadRootContents();
      this.loadFolderTree();
      this.hideLoader();
    }
  }

  loadRootContents() {
    this.showLoader();
    this.fileService.getAllFolders().subscribe({
      next: (folders) => {
        this.currentItems = folders.map((folder) => ({
          ...folder,
          isFolder: true,
          creationDate: folder.createdDate || new Date(),
          size: '0',
          extension: ''
        }));
        this.hideLoader();
      },
      error: (error) => {
        console.error('Error loading root folders:', error);
        this.message = 'Error loading folders';
        this.hideLoader();
      }
    });
  }

  loadFolderContentsByPath(path: string) {
    this.showLoader();
    this.fileService.getFolderContents(path).subscribe({
      next: (folderData) => {
        // Combine folders and files into a single array for display
        const folders = (folderData.folders || []).map((f) => ({
          ...f,
          isFolder: true,
          type: 'folder',
          creationDate: f.createdDate || new Date()
        }));

        const files = (folderData.files || []).map((f) => ({
          ...f,
          isFolder: false,
          creationDate: f.createdDate || new Date()
        }));

        // Sort: folders first, then files
        this.currentItems = [...folders, ...files].sort((a, b) => {
          if (a.isFolder && !b.isFolder) return -1;
          if (!a.isFolder && b.isFolder) return 1;
          return a.name.localeCompare(b.name);
        });

        this.hideLoader();
      },
      error: (error) => {
        console.error('Error loading folder contents:', error);
        this.message = 'Error loading folder contents';
        this.hideLoader();
      }
    });
  }

  loadFolderTree() {
    if (this.Admin === 'MyFile') {
      // Load user's folder structure recursively
      this.fileService.getUserFolder().subscribe({
        next: (userFolder) => {
          const treeData: TreeNode[] = [
            {
              id: userFolder.id,
              name: userFolder.name,
              isFolder: true,
              level: 0,
              path: userFolder.path,
              folderMinioId: userFolder.folderMinioId,
              children: []
            }
          ];
          this.buildTreeChildren(userFolder.path, treeData[0]);
          this.dataSource.data = treeData;
        },
        error: (error) => {
          console.error('Error loading user tree:', error);
        }
      });
    } else {
      // Load all buckets/top-level folders for admin
      this.fileService.getAllFolders().subscribe({
        next: (folders) => {
          const treeData: TreeNode[] = folders.map((folder) => ({
            id: folder.id,
            name: folder.name,
            isFolder: true,
            level: 0,
            path: folder.path,
            folderMinioId: folder.folderMinioId,
            children: []
          }));

          // Load children for each root folder
          treeData.forEach((node) => {
            this.buildTreeChildren(node.path!, node);
          });

          this.dataSource.data = treeData;
        },
        error: (error) => {
          console.error('Error loading admin tree:', error);
        }
      });
    }
  }

  buildTreeChildren(path: string, parentNode: TreeNode) {
    this.fileService.getFolderContents(path).subscribe({
      next: (folderData) => {
        parentNode.children = (folderData.folders || []).map((subFolder) => ({
          id: subFolder.id,
          name: subFolder.name,
          isFolder: true,
          level: (parentNode.level || 0) + 1,
          path: subFolder.path,
          folderMinioId: subFolder.folderMinioId,
          children: []
        }));

        // Recursively build children for each subfolder
        parentNode.children.forEach((child) => {
          this.buildTreeChildren(child.path!, child);
        });
      },
      error: (error) => {
        console.error(`Error building tree for path ${path}:`, error);
      }
    });
  }

  hasChild = (_: number, node: TreeNode) => node.isFolder && node.children && node.children.length > 0;

  onNodeClick(node: TreeNode) {
    if (node.isFolder && node.path) {
      this.selectedNode = node;

      // Update folder stack
      const stackIndex = this.folderStack.findIndex((f) => f.id === node.id);
      if (stackIndex !== -1) {
        this.folderStack = this.folderStack.slice(0, stackIndex + 1);
      } else {
        this.folderStack.push({ id: node.id, name: node.name, path: node.path });
      }

      this.currentFolderPath = node.path;
      this.loadFolderContentsByPath(node.path);
      this.treeControl.expand(node);
    }
  }

  next(element: any) {
    this.message = '';
    this.currentFile = null;
    this.add = false;

    if (element.isFolder && element.path) {
      this.showLoader();

      // Update folder stack
      this.folderStack.push({ id: element.id, name: element.name, path: element.path });
      this.currentFolderPath = element.path;

      this.loadFolderContentsByPath(element.path);

      // Find and expand node in tree
      const node = this.findNodeInTree(element.id, this.dataSource.data);
      if (node) {
        this.selectedNode = node;
        this.expandToNode(node);
      }
    } else if (!element.isFolder) {
      // Open file
      this.openFile(element);
    }
  }

  back() {
    this.message = '';
    this.currentFile = null;
    this.add = false;

    if (this.folderStack.length > 1) {
      this.folderStack.pop();
      const previousFolder = this.folderStack[this.folderStack.length - 1];
      this.currentFolderPath = previousFolder.path;

      if (this.currentFolderPath) {
        this.loadFolderContentsByPath(this.currentFolderPath);
      } else {
        this.loadRootContents();
      }

      // Update tree selection
      if (this.currentFolderPath) {
        const node = this.findNodeInTree(previousFolder.id!, this.dataSource.data);
        if (node) {
          this.selectedNode = node;
        }
      }
    }
  }

  findNodeInTree(id: string, nodes: TreeNode[]): TreeNode | null {
    for (const node of nodes) {
      if (node.id === id) {
        return node;
      }
      if (node.children && node.children.length) {
        const result = this.findNodeInTree(id, node.children);
        if (result) return result;
      }
    }
    return null;
  }

  expandToNode(node: TreeNode) {
    const expandParents = (currentNode: TreeNode) => {
      const parent = this.findParentNode(currentNode.id, this.dataSource.data);
      if (parent) {
        this.treeControl.expand(parent);
        expandParents(parent);
      }
    };
    expandParents(node);
    this.treeControl.expand(node);
  }

  findParentNode(childId: string, nodes: TreeNode[]): TreeNode | null {
    for (const node of nodes) {
      if (node.children && node.children.some((child) => child.id === childId)) {
        return node;
      }
      if (node.children && node.children.length) {
        const result = this.findParentNode(childId, node.children);
        if (result) return result;
      }
    }
    return null;
  }

  addFolder() {
    if (!this.add) {
      this.add = true;
      setTimeout(() => this.focusMyInput(), 100);
    } else {
      this.add = false;
    }
  }

  onEnter() {
    if (this.folderName && this.folderName.trim()) {
      this.showLoader();

      if (!this.currentFolderPath) {
        this.message = 'Please select a parent folder';
        this.hideLoader();
        this.add = false;
        return;
      }

      this.fileService.createFolderByPath(this.currentFolderPath, this.folderName.trim()).subscribe({
        next: () => {
          this.hideLoader();
          this.add = false;
          this.folderName = '';
          this.message = 'Folder created successfully';
          setTimeout(() => (this.message = ''), 3000);

          // Refresh current view
          this.loadFolderContentsByPath(this.currentFolderPath);
          // Refresh tree
          this.loadFolderTree();
        },
        error: (error) => {
          console.error('Error creating folder:', error);
          this.hideLoader();
          this.message = error.error?.message || 'Error creating folder';
          this.add = false;
        }
      });
    }
    this.add = false;
  }

  deleteFolder() {
    if (this.selectedElement) {
      const confirmDelete = confirm(`Are you sure you want to delete ${this.selectedElement.name}?`);
      if (!confirmDelete) return;

      this.showLoader();

      if (this.selectedElement.isFolder) {
        // Delete folder by path or name
        const folderPath = this.selectedElement.path;
        this.fileService.deleteFolder(folderPath).subscribe({
          next: () => {
            this.hideLoader();
            this.message = 'Folder deleted successfully';
            setTimeout(() => (this.message = ''), 3000);
            this.loadFolderContentsByPath(this.currentFolderPath);
            this.loadFolderTree();
          },
          error: (error) => {
            console.error('Error deleting folder:', error);
            this.hideLoader();
            this.message = error.error?.message || 'Error deleting folder';
          }
        });
      } else {
        // Delete file by name (not by ID)
        this.fileService.deleteFile(this.selectedElement.name).subscribe({
          next: () => {
            this.hideLoader();
            this.message = 'File deleted successfully';
            setTimeout(() => (this.message = ''), 3000);
            this.loadFolderContentsByPath(this.currentFolderPath);
          },
          error: (error) => {
            console.error('Error deleting file:', error);
            this.hideLoader();
            this.message = error.error?.message || 'Error deleting file';
          }
        });
      }
    }
  }

  rename() {
    if (this.selectedElement && !this.selectedElement.isFolder) {
      const newName = prompt('Enter new name:', this.selectedElement.name);
      if (newName && newName !== this.selectedElement.name) {
        this.showLoader();
        // Use oldFileName and newFileName instead of fileId and newName
        this.fileService.renameFile(this.selectedElement.name, newName).subscribe({
          next: () => {
            this.hideLoader();
            this.message = 'File renamed successfully';
            setTimeout(() => (this.message = ''), 3000);
            this.loadFolderContentsByPath(this.currentFolderPath);
            this.loadFolderTree(); // Refresh tree to show updated name
          },
          error: (error) => {
            console.error('Error renaming:', error);
            this.hideLoader();
            this.message = error.error?.message || 'Error renaming file';
          }
        });
      }
    } else if (this.selectedElement && this.selectedElement.isFolder) {
      // Optional: Add folder rename functionality
      const newName = prompt('Enter new folder name:', this.selectedElement.name);
      if (newName && newName !== this.selectedElement.name) {
        this.showLoader();
        // You'll need to implement folder rename in backend if needed
        this.fileService.renameFolder(this.selectedElement.path, newName).subscribe({
          next: () => {
            this.hideLoader();
            this.message = 'Folder renamed successfully';
            setTimeout(() => (this.message = ''), 3000);
            this.loadFolderContentsByPath(this.currentFolderPath);
            this.loadFolderTree();
          },
          error: (error) => {
            console.error('Error renaming folder:', error);
            this.hideLoader();
            this.message = error.error?.message || 'Error renaming folder';
          }
        });
      }
    }
  }

  selectFile(event: any) {
    this.currentFile = event.target.files.item(0);
    this.message = this.currentFile ? `Selected: ${this.currentFile.name}` : '';
  }

  preUpload() {
    if (this.currentFile) {
      this.upload();
    }
  }

  upload() {
    if (this.currentFile && this.currentFolderPath !== undefined) {
      this.showLoader();

      this.fileService.uploadFile(this.currentFile, this.currentFolderPath).subscribe({
        next: () => {
          this.hideLoader();
          this.message = `${this.currentFile!.name} uploaded successfully`;
          this.currentFile = null;
          if (this.myInputVariable) {
            this.myInputVariable.nativeElement.value = '';
          }
          this.loadFolderContentsByPath(this.currentFolderPath);
          setTimeout(() => (this.message = ''), 3000);
        },
        error: (error) => {
          console.error('Error uploading file:', error);
          this.hideLoader();
          this.message = error.error?.message || 'Error uploading file';
        }
      });
    }
  }

  downloadFile() {
    if (this.selectedElement && !this.selectedElement.isFolder) {
      this.showLoader();
      const fileName = this.selectedElement.name;

      this.fileService.downloadFile(fileName).subscribe({
        next: (blob: Blob) => {
          // Create a blob URL and trigger download
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = fileName;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);

          this.hideLoader();
          this.message = 'File downloaded successfully';
          setTimeout(() => (this.message = ''), 3000);
        },
        error: (error) => {
          console.error('Error downloading file:', error);
          this.hideLoader();
          this.message = error.error?.message || 'Error downloading file';
        }
      });
    }
  }

  openFile(element: any) {
    this.selectedElement = element;
    this.downloadFile();
  }

  openfileNew(element: any) {
    this.openFile(element);
  }

  onContextMenu(event: MouseEvent, element: any) {
    event.preventDefault();
    this.contextMenuPosition.x = event.clientX + 'px';
    this.contextMenuPosition.y = event.clientY + 'px';
    this.selectedElement = element;
    if (this.contextMenu) {
      this.contextMenu.openMenu();
    }
  }

  focusMyInput() {
    setTimeout(() => {
      const input = document.getElementById('myInput');
      if (input) {
        input.focus();
      }
    }, 100);
  }

  showLoader() {
    this.isLoading = true;
  }

  hideLoader() {
    this.isLoading = false;
  }

  checkPermissions(code: string): boolean {
    return true;
  }

  isFolder(): boolean {
    return this.selectedElement?.isFolder === true;
  }

  isFile(): boolean {
    return this.selectedElement?.isFolder === false;
  }

  toggleTreeVisibility() {
    this.isTreeVisible = !this.isTreeVisible;
  }

  getTooltipTextFolder(name: string, creationDate: string): string {
    return `Nom: ${name}, Date de creation: ${creationDate}`;
  }

  getTooltipTextFile(name: string, creationDate: string, size: string, type: string): string {
    return `Nom: ${name}, Date de creation: ${creationDate}, Taille: ${size} Ko, Type: ${type}`;
  }

  changeValue() {
    this.selectedUserEx = null;
  }

  findFolderByUserName(username: string) {
    console.log('Finding folder for user:', username);
  }

  refresh() {
    if (this.currentFolderPath) {
      this.loadFolderContentsByPath(this.currentFolderPath);
    } else {
      this.loadRootContents();
    }
    this.loadFolderTree();
  }

  navigationpath(): boolean {
    const currentPath = this.folderStack.map((f) => f.name).join('/');
    return currentPath.includes('/request/');
  }
}
