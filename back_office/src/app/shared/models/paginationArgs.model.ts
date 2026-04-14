export interface PaginationArgs {
  page?: number;
  size?: number;
  sort?: string;
}

export class PaginationSortArgs {
  private property: string;
  private direction: PaginationSortOrderType;
  private readonly _sort: string;
  constructor(property: string, direction: PaginationSortOrderType) {
    this.property = property;
    this.direction = direction;
    this._sort = property + ',' + direction;
  }
  get sort(): string {
    return this._sort;
  }
}

export enum PaginationSortOrderType {
  ASC = 'ASC',
  DESC = 'DESC',
}

export interface PaginationPage<T> {
  content: T[];
  last?: boolean;
  totalElements?: number;
  totalPages?: number;
  size?: number;
  number?: number;
  first?: boolean;
  sort?: PaginationSortArgs[];
}
