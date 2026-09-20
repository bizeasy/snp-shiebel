import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Attachment e2e test', () => {
  const attachmentPageUrl = '/attachment';
  let username: string;
  let password: string;
  const attachmentSample = { name: 'um ugh' };

  let attachment;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/attachments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (attachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/attachments/${attachment.id}`,
      }).then(() => {
        attachment = undefined;
      });
    }
  });

  it('Attachments menu should load Attachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Attachment').should('exist');
    cy.location('pathname').should('eq', attachmentPageUrl);
  });

  describe('Attachment page', () => {
    it('should have translated page title', () => {
      cy.visit(attachmentPageUrl);
      cy.getEntityHeading('Attachment').should('not.contain', 'snpApp.attachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(attachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Attachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${attachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Attachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', attachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/attachments',
          body: attachmentSample,
        }).then(({ body }) => {
          attachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/attachments?page=0&size=20>; rel="last",<http://localhost/api/attachments?page=0&size=20>; rel="first"',
              },
              body: [attachment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(attachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Attachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('attachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', attachmentPageUrl);
      });

      it('edit button click should load edit Attachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Attachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', attachmentPageUrl);
      });

      it('edit button click should load edit Attachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Attachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', attachmentPageUrl);
      });

      it('last delete button click should delete instance of Attachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('attachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', attachmentPageUrl);

        attachment = undefined;
      });
    });
  });

  describe('new Attachment page', () => {
    beforeEach(() => {
      cy.visit(attachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Attachment');
    });

    it('should create an instance of Attachment', () => {
      cy.get(`[data-cy="name"]`).type('ah bungalow');
      cy.get(`[data-cy="name"]`).should('have.value', 'ah bungalow');

      cy.setFieldImageAsBytesOfEntity('fileAttachment', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="attachmentUrl"]`).type('instantly fooey until');
      cy.get(`[data-cy="attachmentUrl"]`).should('have.value', 'instantly fooey until');

      cy.get(`[data-cy="mimeType"]`).type('uh-huh yet');
      cy.get(`[data-cy="mimeType"]`).should('have.value', 'uh-huh yet');

      cy.get(`[data-cy="fileSize"]`).type('12370');
      cy.get(`[data-cy="fileSize"]`).should('have.value', '12370');

      cy.get(`[data-cy="uploadedDate"]`).type('2023-12-14T07:06');
      cy.get(`[data-cy="uploadedDate"]`).blur();
      cy.get(`[data-cy="uploadedDate"]`).should('have.value', '2023-12-14T07:06');

      cy.get(`[data-cy="isTrashed"]`).should('not.be.checked');
      cy.get(`[data-cy="isTrashed"]`).click();
      cy.get(`[data-cy="isTrashed"]`).should('be.checked');

      cy.get(`[data-cy="checksum"]`).type('upside-down');
      cy.get(`[data-cy="checksum"]`).should('have.value', 'upside-down');

      // The blob fields are validated asynchronously; wait for the form to become valid
      // (save button enabled) instead of a fixed delay before submitting.
      cy.get(entityCreateSaveButtonSelector).should('be.enabled');
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        attachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', attachmentPageUrl);
    });
  });
});
