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

describe('FileShare e2e test', () => {
  const fileSharePageUrl = '/file-share';
  let username: string;
  let password: string;
  const fileShareSample = { permission: 'OWNER', sharedDate: '2023-12-14T03:27:04.772Z' };

  let fileShare;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/file-shares+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/file-shares').as('postEntityRequest');
    cy.intercept('DELETE', '/api/file-shares/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fileShare) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/file-shares/${fileShare.id}`,
      }).then(() => {
        fileShare = undefined;
      });
    }
  });

  it('FileShares menu should load FileShares page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-share');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FileShare').should('exist');
    cy.location('pathname').should('eq', fileSharePageUrl);
  });

  describe('FileShare page', () => {
    it('should have translated page title', () => {
      cy.visit(fileSharePageUrl);
      cy.getEntityHeading('FileShare').should('not.contain', 'snpApp.fileShare.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fileSharePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FileShare page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fileSharePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FileShare');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileSharePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/file-shares',
          body: fileShareSample,
        }).then(({ body }) => {
          fileShare = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/file-shares+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/file-shares?page=0&size=20>; rel="last",<http://localhost/api/file-shares?page=0&size=20>; rel="first"',
              },
              body: [fileShare],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fileSharePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FileShare page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fileShare');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileSharePageUrl);
      });

      it('edit button click should load edit FileShare page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FileShare');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileSharePageUrl);
      });

      it('edit button click should load edit FileShare page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FileShare');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileSharePageUrl);
      });

      it('last delete button click should delete instance of FileShare', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fileShare').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileSharePageUrl);

        fileShare = undefined;
      });
    });
  });

  describe('new FileShare page', () => {
    beforeEach(() => {
      cy.visit(fileSharePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FileShare');
    });

    it('should create an instance of FileShare', () => {
      cy.get(`[data-cy="permission"]`).select('EDIT');

      cy.get(`[data-cy="sharedWithEmail"]`).type('saloon executor');
      cy.get(`[data-cy="sharedWithEmail"]`).should('have.value', 'saloon executor');

      cy.get(`[data-cy="sharedDate"]`).type('2023-12-14T15:22');
      cy.get(`[data-cy="sharedDate"]`).blur();
      cy.get(`[data-cy="sharedDate"]`).should('have.value', '2023-12-14T15:22');

      cy.get(`[data-cy="expiryDate"]`).type('2023-12-14T18:28');
      cy.get(`[data-cy="expiryDate"]`).blur();
      cy.get(`[data-cy="expiryDate"]`).should('have.value', '2023-12-14T18:28');

      cy.get(`[data-cy="accessToken"]`).type('perspire teeming bookend');
      cy.get(`[data-cy="accessToken"]`).should('have.value', 'perspire teeming bookend');

      cy.get(`[data-cy="isRevoked"]`).should('not.be.checked');
      cy.get(`[data-cy="isRevoked"]`).click();
      cy.get(`[data-cy="isRevoked"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fileShare = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fileSharePageUrl);
    });
  });
});
