import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'truncateFileName' })
export class FileNamePipe implements PipeTransform {
  transform(fileName: string, maxLength: number): string {
    if (fileName.length <= maxLength) {
      return fileName;
    }

    const extensionIndex = fileName.lastIndexOf('.');
    const name = fileName.substring(0, extensionIndex);
    const extension = fileName.substring(extensionIndex);
    const truncatedName = name.substring(0, maxLength - extension.length - 1);
    return truncatedName + '...' + extension;
  }
}
