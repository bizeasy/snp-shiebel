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

describe('ContentType e2e test', () => {
  const contentTypePageUrl = '/content-type';
  let username: string;
  let password: string;
  const contentTypeSample = { name: 'anenst' };

  let contentType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/content-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/content-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/content-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/content-types/${contentType.id}`,
      }).then(() => {
        contentType = undefined;
      });
    }
  });

  it('ContentTypes menu should load ContentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('content-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContentType').should('exist');
    cy.location('pathname').should('eq', contentTypePageUrl);
  });

  describe('ContentType page', () => {
    it('should have translated page title', () => {
      cy.visit(contentTypePageUrl);
      cy.getEntityHeading('ContentType').should('not.contain', 'snpApp.contentType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContentType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${contentTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ContentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contentTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/content-types',
          body: contentTypeSample,
        }).then(({ body }) => {
          contentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/content-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [contentType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contentType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contentTypePageUrl);
      });

      it('edit button click should load edit ContentType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contentTypePageUrl);
      });

      it('edit button click should load edit ContentType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContentType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contentTypePageUrl);
      });

      it('last delete button click should delete instance of ContentType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contentTypePageUrl);

        contentType = undefined;
      });
    });
  });

  describe('new ContentType page', () => {
    beforeEach(() => {
      cy.visit(contentTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContentType');
    });

    it('should create an instance of ContentType', () => {
      cy.get(`[data-cy="name"]`).type('ack elliptical');
      cy.get(`[data-cy="name"]`).should('have.value', 'ack elliptical');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contentType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', contentTypePageUrl);
    });
  });
});
