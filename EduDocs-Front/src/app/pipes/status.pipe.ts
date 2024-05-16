import { Pipe, PipeTransform } from '@angular/core';
import { RequestStatus } from '../services/storage.service';

@Pipe({
  name: 'status'
})
export class StatusPipe implements PipeTransform {

  transform(value: number, ...args: unknown[]): unknown {
    return RequestStatus[value]
  }

}
