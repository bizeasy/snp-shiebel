import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusValidChangeService } from '../service/status-valid-change.service';
import { IStatusValidChange } from '../status-valid-change.model';

import { StatusValidChangeFormService } from './status-valid-change-form.service';
import { StatusValidChangeUpdate } from './status-valid-change-update';

describe('StatusValidChange Management Update Component', () => {
  let comp: StatusValidChangeUpdate;
  let fixture: ComponentFixture<StatusValidChangeUpdate>;
  let activatedRoute: ActivatedRoute;
  let statusValidChangeFormService: StatusValidChangeFormService;
  let statusValidChangeService: StatusValidChangeService;
  let statusService: StatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(StatusValidChangeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusValidChangeFormService = TestBed.inject(StatusValidChangeFormService);
    statusValidChangeService = TestBed.inject(StatusValidChangeService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Status query and add missing value', () => {
      const statusValidChange: IStatusValidChange = { id: 30744 };
      const status: IStatus = { id: 2927 };
      statusValidChange.status = status;
      const statusTo: IStatus = { id: 2927 };
      statusValidChange.statusTo = statusTo;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status, statusTo];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ statusValidChange });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const statusValidChange: IStatusValidChange = { id: 30744 };
      const status: IStatus = { id: 2927 };
      statusValidChange.status = status;
      const statusTo: IStatus = { id: 2927 };
      statusValidChange.statusTo = statusTo;

      activatedRoute.data = of({ statusValidChange });
      comp.ngOnInit();

      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.statusesSharedCollection()).toContainEqual(statusTo);
      expect(comp.statusValidChange).toEqual(statusValidChange);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStatusValidChange>();
      const statusValidChange = { id: 8787 };
      vi.spyOn(statusValidChangeFormService, 'getStatusValidChange').mockReturnValue(statusValidChange);
      vi.spyOn(statusValidChangeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusValidChange });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(statusValidChange);
      saveSubject.complete();

      // THEN
      expect(statusValidChangeFormService.getStatusValidChange).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusValidChangeService.update).toHaveBeenCalledWith(expect.objectContaining(statusValidChange));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStatusValidChange>();
      const statusValidChange = { id: 8787 };
      vi.spyOn(statusValidChangeFormService, 'getStatusValidChange').mockReturnValue({ id: null });
      vi.spyOn(statusValidChangeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusValidChange: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(statusValidChange);
      saveSubject.complete();

      // THEN
      expect(statusValidChangeFormService.getStatusValidChange).toHaveBeenCalled();
      expect(statusValidChangeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IStatusValidChange>();
      const statusValidChange = { id: 8787 };
      vi.spyOn(statusValidChangeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusValidChange });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusValidChangeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
