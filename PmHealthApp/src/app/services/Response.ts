export class Response {
  constructor(
    public code: number,
    public message: string,
    public entity: any
  ) { }
}
