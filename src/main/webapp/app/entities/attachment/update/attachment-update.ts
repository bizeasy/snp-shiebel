import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IContentType } from 'app/entities/content-type/content-type.model';
import { ContentTypeService } from 'app/entities/content-type/service/content-type.service';
import { IFileFolder } from 'app/entities/file-folder/file-folder.model';
import { FileFolderService } from 'app/entities/file-folder/service/file-folder.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { IAttachment } from '../attachment.model';
import { AttachmentService } from '../service/attachment.service';

import { AttachmentFormGroup, AttachmentFormService } from './attachment-form.service';

@Component({
  selector: 'sys-attachment-update',
  templateUrl: './attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class AttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  attachment: IAttachment | null = null;

  contentTypesSharedCollection = signal<IContentType[]>([]);
  fileFoldersSharedCollection = signal<IFileFolder[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected attachmentService = inject(AttachmentService);
  protected attachmentFormService = inject(AttachmentFormService);
  protected contentTypeService = inject(ContentTypeService);
  protected fileFolderService = inject(FileFolderService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AttachmentFormGroup = this.attachmentFormService.createAttachmentFormGroup();

  compareContentType = (o1: IContentType | null, o2: IContentType | null): boolean => this.contentTypeService.compareContentType(o1, o2);

  compareFileFolder = (o1: IFileFolder | null, o2: IFileFolder | null): boolean => this.fileFolderService.compareFileFolder(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachment }) => {
      this.attachment = attachment;
      if (attachment) {
        this.updateForm(attachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined, blobType?: BlobType): void {
    this.dataUtils.openFile(base64String, contentType, blobType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('snpApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const attachment = this.attachmentFormService.getAttachment(this.editForm);
    if (attachment.id === null) {
      this.subscribeToSaveResponse(this.attachmentService.create(attachment));
    } else {
      this.subscribeToSaveResponse(this.attachmentService.update(attachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAttachment | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(attachment: IAttachment): void {
    this.attachment = attachment;
    this.attachmentFormService.resetForm(this.editForm, attachment);

    this.contentTypesSharedCollection.update(contentTypes =>
      this.contentTypeService.addContentTypeToCollectionIfMissing<IContentType>(contentTypes, attachment.contentType),
    );
    this.fileFoldersSharedCollection.update(fileFolders =>
      this.fileFolderService.addFileFolderToCollectionIfMissing<IFileFolder>(fileFolders, attachment.folder),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, attachment.owner));
  }

  protected loadRelationshipsOptions(): void {
    this.contentTypeService
      .query()
      .pipe(map((res: HttpResponse<IContentType[]>) => res.body ?? []))
      .pipe(
        map((contentTypes: IContentType[]) =>
          this.contentTypeService.addContentTypeToCollectionIfMissing<IContentType>(contentTypes, this.attachment?.contentType),
        ),
      )
      .subscribe((contentTypes: IContentType[]) => this.contentTypesSharedCollection.set(contentTypes));

    this.fileFolderService
      .query()
      .pipe(map((res: HttpResponse<IFileFolder[]>) => res.body ?? []))
      .pipe(
        map((fileFolders: IFileFolder[]) =>
          this.fileFolderService.addFileFolderToCollectionIfMissing<IFileFolder>(fileFolders, this.attachment?.folder),
        ),
      )
      .subscribe((fileFolders: IFileFolder[]) => this.fileFoldersSharedCollection.set(fileFolders));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.attachment?.owner)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
