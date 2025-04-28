import {Injectable} from '@angular/core';
import {delay, Observable, of} from 'rxjs';
import {SearchCriteriaDto, SearchResultDto} from '../shared/models/base-models';
import {ProductCriteria} from './components/toolbox/toolbox.component';

export interface Product {
  id?: string;
  date?: string;
  status?: string;
  amount?: string;
}

const products: Product[] = [
  {
    id: '1000',
    date: '2023-01-01', // Replace with actual date values
    status: 'success',
    amount: '-500'
  },
  {
    id: '1001',
    date: '2023-02-15',
    status: 'success',
    amount: '-500'
  },
  {
    id: '1002',
    date: '2023-03-10',
    status: 'success',
    amount: '-500'
  },
  {
    id: '1003',
    date: '2023-04-22',
    status: 'failed',
    amount: '-500'
  },
  {
    id: '1004',
    date: '2023-05-30',
    status: 'success',
    amount: '-500'
  },
  {
    id: '1005',
    date: '2023-06-11',
    status: 'success',
    amount: '-500'
  },
  {
    id: '1006',
    date: '2023-08-19',
    status: 'failed',
    amount: '-500'
  },
  {
    id: '1007',
    date: '2023-08-19',
    status: 'success',
    amount: '-500'
  },
  {
    id: '1008',
    date: '2023-09-25',
    status: 'success',
    amount: '-500'
  },
  {
    id: '1009',
    date: '2023-10-14',
    status: 'success',
    amount: '-500'
  },
  {
    id: '1010',
    date: '2023-11-07',
    status: 'success',
    amount: '-500'
  }
];

@Injectable({
  providedIn: 'root'
})
export class DevService {
  constructor() {}

  getData(searchCriteria: SearchCriteriaDto<ProductCriteria>): Observable<SearchResultDto<Product>> {
    console.log('getData - searchCriteria: ', searchCriteria.page, searchCriteria.sort, searchCriteria.criteria);
    return of({
      results: products.slice(
        searchCriteria.page.pageNumber,
        searchCriteria.page.pageNumber + searchCriteria.page.pageSize
      ),
      totalElements: products.length
    }).pipe(delay(500));
  }
}
