import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'textFilter'
})
export class TextFilterPipe implements PipeTransform {

  transform(value: string[], filter: string): any {
    if(typeof filter === 'undefined' || filter === null || filter === '') {
      return value;
    }
    return value.filter( value => value.indexOf(filter) > -1 );
  }

}
