export interface Variable<T> {
  name: string;
  type: string;
  value: T;
}

export type VariableList = Variable<any>[];
