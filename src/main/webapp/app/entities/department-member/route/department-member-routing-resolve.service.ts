import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IDepartmentMember } from '../department-member.model';
import { DepartmentMemberService } from '../service/department-member.service';

const departmentMemberResolve = (route: ActivatedRouteSnapshot): Observable<null | IDepartmentMember> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(DepartmentMemberService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default departmentMemberResolve;
